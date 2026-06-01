using NKS.Interactive.RemotePlayer.Server.Services;
using NKS.Interactive.RemotePlayer.Server.Utils;

var builder = WebApplication.CreateBuilder(args);

string path = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), "VkAudioPlayer", "data.txt");
string accessToken;
long userId;
string savePath;
float volume = 1;

if (!File.Exists(path))
{
    Console.WriteLine("Authorization instruction:");
    Console.WriteLine("Open link: https://oauth.vk.com/authorize?client_id=6287487&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=audio,offline&response_type=token&v=5.131");
    Console.Write(" after confirmation, copy your new URL:");
    string url = Console.ReadLine();
    (string? t, long? u) = TokenParser.Parse(url);
    accessToken = t ?? string.Empty;
    userId = u ?? 0L;
    if (t == null)
    {
        Console.WriteLine("Invalid token");
        return;
    }
    Console.WriteLine("Enter save folder path: ");
    savePath = Console.ReadLine();

    Directory.CreateDirectory(Path.GetDirectoryName(path)!);
    File.WriteAllLines(path, [accessToken, userId.ToString(), savePath, "1"]);
}
else
{
    string[] content = File.ReadAllLines(path);
    accessToken = content[0];
    userId = Convert.ToInt64(content[1]);
    savePath = content[2];
    if (content.Length > 3)
        volume = float.Parse(content[3]);
    else
        volume = 1f;
}

Console.WriteLine("Загружаю музыку...");
var audioService = new AudioService(savePath, accessToken, userId,volume);
try
{
    await audioService.Load();
}
catch(Exception ex)
{
    if(ex is VkNet.Exception.UserAuthorizationFailException)
    {
        Console.WriteLine("Authorization failed...");
        Console.WriteLine("Authorization instruction:");
        Console.WriteLine("Open link: https://oauth.vk.com/authorize?client_id=6287487&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=audio,offline&response_type=token&v=5.131");
        Console.Write(" after confirmation, copy your new URL:");
        string url = Console.ReadLine();
        (string? t, long? u) = TokenParser.Parse(url);
        accessToken = t ?? string.Empty;
        userId = u ?? 0L;
        if (t == null)
        {
            Console.WriteLine("Invalid token");
            return;
        }

        Directory.CreateDirectory(Path.GetDirectoryName(path)!);
        File.WriteAllLines(path, [accessToken, userId.ToString(), savePath]);
        Console.WriteLine("Повторная попытка загрузить музыку...");
        audioService = new AudioService(savePath, accessToken, userId, volume);
        await audioService.Load();
    }
}

builder.Services.AddControllers();
builder.Services.AddSingleton(audioService);

var app = builder.Build();
app.MapControllers();

app.Run("http://0.0.0.0:5000");

