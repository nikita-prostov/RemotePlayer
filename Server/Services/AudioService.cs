using NKS.Interactive.RemotePlayer.Server.Models;
using VkNet;
using VkNet.Enums.Filters;
using VkNet.Model;

namespace NKS.Interactive.RemotePlayer.Server.Services
{
    public class AudioService(string savePath, string accessToken, long userId, float volume)
    {
        private VkApi api = new VkApi();
        private List<Audio> audios = [];
        private List<Audio> shuffledAudios = [];
        private bool useShuffled = false;
        private int current = 0;
        private bool isStarted = false;
        private AudioPlayer player = new AudioPlayer(savePath,volume);

        private readonly string accessToken = accessToken;
        private readonly long userId = userId;

        public AudioPlayer Player => player;
        public TrackInfo? CurrentTrack => player.CurrentTrack is null ? null : new TrackInfo
        {
            Artist = player.CurrentTrack.Artist,
            Title = player.CurrentTrack.Title,
        };

        public ServerState State => new ServerState
        {
            CurrentTrack = CurrentTrack,
            Volume = player.Volume,
            IsPlaying = player.IsPlaying,
            IsShuffled = useShuffled
        };

        public async Task Start()
        {
            if (!isStarted)
            {
                var track = useShuffled ? shuffledAudios[current] : audios[current];
                await player.PlayAsync(track.Url.ToString(), track);
                player.PlaybackStopped += Player_PlayNext;
                isStarted = true;
            }
        }

        private async void Player_PlayNext()
        {
            await Next();
        }

        public async Task Next()
        {
            current++;
            if (current >= audios.Count) current = 0;
            var track = useShuffled ? shuffledAudios[current] : audios[current];
            await player.PlayAsync(track.Url.ToString(), track);
        }

        public async Task Prev()
        {
            current--;
            if (current < 0) current = audios.Count - 1;
            var track = useShuffled ? shuffledAudios[current] : audios[current];
            await player.PlayAsync(track.Url.ToString(), track);
        }

        public async Task Play() 
        {
            if(player.CurrentTrack == null && !player.IsPlaying)
            {
                var track = useShuffled ? shuffledAudios[current] : audios[current];
                await player.PlayAsync(track.Url.ToString(), track);
                player.PlaybackStopped += Player_PlayNext;
                isStarted = true;
            }
            else
            {
                player.Play();
            }
        }

        public void Pause() => player.Pause();

        public void SetVolume(float value)
        {
            float tolerance = 0.1f;
            float minValue = player.Volume - tolerance;
            float maxValue = player.Volume + tolerance;
            if (value > maxValue || value < minValue)
            {
                string path = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), "VkAudioPlayer", "data.txt");
                List<string> content = File.ReadAllLines(path).ToList();
                if (content.Count == 4)
                {
                    content[4] = value.ToString();
                }
                else
                {
                    content.Add(value.ToString());
                }
                player.Volume = value;
                File.WriteAllLines(path, content);
            }
            
        }

        public void Shuffle()
        {
            player.Stop();
            current = 0;
            shuffledAudios.Clear();
            shuffledAudios.AddRange(audios);
            Random rng = new Random();
            int n = shuffledAudios.Count;
            useShuffled = true;
            while (n > 1)
            {
                n--;
                int k = rng.Next(n + 1);
                (shuffledAudios[k], shuffledAudios[n]) = (shuffledAudios[n], shuffledAudios[k]);
            }
        }

        public void Sort()
        {
            player.Stop();
            current = 0;
            shuffledAudios.Clear();
            useShuffled = false;
        }

        public async Task<string> Play(TrackInfo trackInfo)
        {
            Audio? audio = null;
            for (int i = 0; i < audios.Count; i++)
            {
                var ca = audios[i];
                if (ca.Title == trackInfo.Title && ca.Artist == trackInfo.Artist)
                {
                    audio = ca;
                    current = i;
                    break;
                }
            }
            if (audio != null)
            {
                await player.PlayAsync(audio.Url.ToString(), audio);
                return "ok";
            }
            return "fail";
        }

        public List<TrackInfo> GetList(int count, int page)
        {
            var list = new List<TrackInfo>();
            for (int i = count * (page - 1); i < count + (count * (page - 1)); i++)
            {
                if (i >= audios.Count) break;
                var audio = useShuffled ? shuffledAudios[i] : audios[i];
                list.Add(new TrackInfo
                {
                    Artist = audio.Artist,
                    Title = audio.Title
                });
            }
            return list;
        }

        private async Task AuthorizeApp()
        {

            await api.AuthorizeAsync(new ApiAuthParams
            {
                UserId = userId,
                AccessToken = accessToken,
                Settings = Settings.Audio
            });
        }

        public async Task<List<TrackInfo>> Load(int count = 100, int page = 1)
        {
            await AuthorizeApp();
            var music = await api.Audio.GetAsync(new AudioGetParams
            {
                OwnerId = api.UserId.Value,
                Count = count,
                Offset = (page - 1) * count
            });
            audios.AddRange(music);
            var list = new List<TrackInfo>();
            foreach (var track in music)
            {
                list.Add(new TrackInfo
                {
                    Artist = track.Artist,
                    Title = track.Title
                });
            }
            return list;
        }

        public TrackInfo GetNext()
        {
            int nextIndex = current + 1;
            if (nextIndex >= audios.Count) nextIndex = 0;

            var track = useShuffled ? shuffledAudios[nextIndex] : audios[nextIndex];
            return new TrackInfo
            {
                Artist = track.Artist,
                Title = track.Title
            };
        }
    }
}
