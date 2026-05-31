using NAudio.Wave;
using VkNet.Model;

namespace NKS.Interactive.RemotePlayer.Server.Services
{
    public class AudioPlayer(string savePath)
    {
        private WaveOutEvent? _outputDevice;
        private Mp3FileReader? _audioReader;
        private AudioConverter converter = new AudioConverter(savePath);
        private float _volume = 1f;

        public Audio? CurrentTrack { get; set; }
        public bool IsPlaying { get; set; }
        public bool HasInternet { get; set; }
        public float Volume
        {
            get => _volume;
            set
            {
                _volume = Math.Clamp(value, 0f, 1f);
                _outputDevice.Volume = _volume;
            }
        }

        public event Action<Audio>? TrackStarted;
        public event Action? PlaybackStopped;

        public async Task PlayAsync(string url, Audio track)
        {
            Stop();
            try
            {
                CurrentTrack = track;
                string filePath = await converter.ConvertAsync(url, track);
                Console.WriteLine($"Playing: {track.Title} - {track.Artist}");

                _audioReader = new Mp3FileReader(filePath);
                _outputDevice = new WaveOutEvent
                {
                    Volume = _volume
                };
                _outputDevice.PlaybackStopped += OnPlaybackStopped;
                _outputDevice.Init(_audioReader);
                _outputDevice.Play();

                IsPlaying = true;

                TrackStarted?.Invoke(track);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
                Stop();
            }
        }

        public void Play()
        {
            _outputDevice?.Play();
            IsPlaying = true;
        }

        public void Pause()
        {
            _outputDevice?.Pause();
            IsPlaying = false;
        }

        public void Stop()
        {
            if (_outputDevice != null)
            {
                _outputDevice.PlaybackStopped -= OnPlaybackStopped;
                _outputDevice.Stop();
                _outputDevice.Dispose();
                _outputDevice = null;
            }

            _audioReader?.Dispose();
            _audioReader = null;

            IsPlaying = false;
            CurrentTrack = null;
        }

        private void OnPlaybackStopped(object? sender, StoppedEventArgs e)
        {
            IsPlaying = false;
            PlaybackStopped?.Invoke();
        }
    }
}