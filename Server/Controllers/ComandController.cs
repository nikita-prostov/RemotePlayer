using Microsoft.AspNetCore.Mvc;
using NKS.Interactive.RemotePlayer.Server.Models;
using NKS.Interactive.RemotePlayer.Server.Services;

namespace NKS.Interactive.RemotePlayer.Server.Controllers
{
    [ApiController]
    [Route("/")]
    public class ComandController(AudioService audioService) : ControllerBase
    {
        [HttpGet("ping")]
        public IActionResult Ping() => Ok();

        [HttpPost("switch")]
        public async Task<IActionResult> Switch([FromQuery] string to)
        {
            if (to == "next") await audioService.Next();
            else if (to == "prev") await audioService.Prev();
            else return BadRequest("Invalid direction");
            return Ok(audioService.CurrentTrack);
        }

        [HttpPost("volume/set")]
        public IActionResult SetVolume([FromQuery] float value)
        {
            audioService.SetVolume(value);
            return NoContent();
        }

        [HttpPost("play")]
        public async Task<IActionResult> Play([FromBody] TrackInfo? trackInfo = null)
        {
            if (trackInfo != null)
            {
                await audioService.Play(trackInfo);
                return Ok(audioService.CurrentTrack);
            }

            if (audioService.CurrentTrack == null)
                await audioService.Start();
            else
                await audioService.Play();

            return Ok(audioService.CurrentTrack);
        }

        [HttpPost("shuffle")]
        public IActionResult Shuffle()
        {
            audioService.Shuffle();
            return Ok("ok");
        }

        [HttpPost("sort")]
        public IActionResult Sort()
        {
            audioService.Sort();
            return Ok("ok");
        }

        [HttpPost("pause")]
        public IActionResult Pause()
        {
            audioService.Pause();
            return Ok("ok");
        }

        [HttpPost("load")]
        public async Task<IActionResult> Load([FromQuery] int count, [FromQuery] int page) => Ok(await audioService.Load(count, page));
        

        [HttpGet("all")]
        public IActionResult GetAll([FromQuery] int count, [FromQuery] int page) => Ok(audioService.GetList(count, page));


        [HttpGet("track/current")]
        public IActionResult GetCurrent()
        {
            if (audioService.CurrentTrack == null)
                return NoContent();
            return Ok(audioService.CurrentTrack);
        }

        [HttpGet("state")]
        public IActionResult GetState()
        {
            return Ok(audioService.State);
        }

        [HttpGet("track/next")]
        public IActionResult GetNextTrack()
        {
            return Ok(audioService.GetNext());
        }

    }
}
