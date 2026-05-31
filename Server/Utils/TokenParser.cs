using System.Web;

namespace NKS.Interactive.RemotePlayer.Server.Utils
{
    internal static class TokenParser
    {
        public static (string? accessToken, long? userId) Parse(string url)
        {
            if (string.IsNullOrEmpty(url))
                return (null, null);

            try
            {
                int hashIndex = url.IndexOf('#');
                if (hashIndex == -1)
                    return (null, null);

                string fragment = url[(hashIndex + 1)..];

                var parameters = HttpUtility.ParseQueryString(fragment);

                string? accessToken = parameters["access_token"];
                string? userIdStr = parameters["user_id"];

                long? userId = long.TryParse(userIdStr, out long id) ? id : null;

                return (accessToken, userId);
            }
            catch
            {
                return (null, null);
            }
        }
    }
}
