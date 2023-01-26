package gerthao.ziohttpexample.apps

import zio.http.*
import zio.*
import zio.http.ChannelEvent.*
import zio.http.ChannelEvent.UserEvent.*
import zio.http.socket.{WebSocketChannelEvent, WebSocketFrame}

object WebSocketApp:

  val app: Http[Any, Throwable, WebSocketChannelEvent, Unit] = Http.collectZIO[WebSocketChannelEvent] {
    case ChannelEvent(channel, ChannelRead(WebSocketFrame.Text(message))) =>
      channel.writeAndFlush(WebSocketFrame.text(message.toUpperCase))
    case ChannelEvent(channel, UserEventTriggered(event)) => event match {
        case HandshakeComplete => Console.printLine("Websocket started...")
        case HandshakeTimeout  =>
          Console.printLine("Connection has timed out...")
      }
    case ChannelEvent(_, ChannelUnregistered)             =>
      Console.printLine("Connection closed...")
  }
