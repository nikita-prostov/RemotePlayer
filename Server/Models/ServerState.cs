namespace NKS.Interactive.RemotePlayer.Server.Models
{
    public class ServerState
    {
        public TrackInfo? CurrentTrack {  get; set; }
        public bool IsPlaying { get; set; }
        public float Volume { get; set; }
        public bool IsShuffled { get; set; }
    }
}
