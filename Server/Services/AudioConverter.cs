using System.Diagnostics;
using VkNet.Model;

namespace NKS.Interactive.RemotePlayer.Server.Services
{
    internal class AudioConverter(string savePath)
    {
        public async Task<string> ConvertAsync(string url, Audio track)
        {
            if (File.Exists(url))
                return url;

            var file = Path.Combine(savePath, $"{SanitizeFileName(track.Title)} - {SanitizeFileName(track.Artist)}.mp3");

            if (File.Exists(file))
                return file;

            Console.WriteLine("Converting to mp3...");

            var process = new Process
            {
                StartInfo = new ProcessStartInfo
                {
                    FileName = "ffmpeg",
                    Arguments = $"-i \"{url}\" -acodec libmp3lame -ab 320k -y \"{file}\"",
                    UseShellExecute = false,
                    RedirectStandardOutput = false,
                    RedirectStandardError = false,
                    CreateNoWindow = true
                }
            };

            process.Start();
            await process.WaitForExitAsync();
            if (process.ExitCode != 0 || !File.Exists(file) || new FileInfo(file).Length == 0)
            {
                Console.WriteLine("Convert failed, playing from URL directly...");
                return url;
            }
            else
            {
                Console.WriteLine("Success converted.");
                return file;
            }
        }

        private static string SanitizeFileName(string name)
        {
            char[] invalid = Path.GetInvalidFileNameChars();
            foreach (char c in invalid)
                name = name.Replace(c, '_');
            return name;
        }
    }
}
