<h1 align="center">
    JSONAPI-Nukkit(X)
</h1>

[![](https://img.shields.io/badge/NUKKIT-1.0-blue?style=flat-square)](https://ci.nukkitx.com/job/NukkitX/job/Nukkit/job/master/)
[![](https://img.shields.io/badge/LICENSE-MIT-green?style=flat-square)](./LICENSE)

# 简介
JSONAPI-Nukkit(X) 是一个用于 Nukkit(X) 服务器的插件。提供了访问服务器各种数据和功能的 HTTP-API 接口。你可以通过这些接口来制作网站、App，以及提供让玩家在线购买物品并且在游戏中自动接收相关物品的能力。

**注意**：JSONAPI-Nukkit(X) 并不提供上述服务，您可以根据 JSONAPI-Nukkit(X) 提供的 API 自行实现相关的功能。

# 重要说明
JSONAPI-Nukkit(X) 还处于早期的**开发中**状态。JSONAPI-Nukkit(X) 的目录结构、API 地址、配置文件格式等均有可能在未来发生变化。**不建议将当前版本的插件用于重要的线上服务器中**。

# 插件说明
JSONAPI-Nukkit(X) 提供了使用 HTTP-API 调用 Nukkit(X) 底层功能的能力，可以实现大部分包括服务器管理、插件管理、用户管理等方面的功能。您可以在 Nukkit(X) 服务器安装有 JSONAPI-Nukkit(X) 的前提下，使用 HTTP-API 开发相关网站、App、商城系统等。也可以以 JSONAPI-Nukkit(X) 提供的 API 为基础，开发类似“云插件”（开发中）的功能（不限编程语言，无需安装至服务器）。

## API 相关信息

### API 文档
**暂无，整理中。可自行查看各个 action 上方的 `ApiRoute` 注解。**

### 调用方式

- 所有 API 均使用 `POST` 方式调用。
- 传递参数放置于 `Query` 中，参数名为 `Data`

### 调用参数

所有 API 的统一调用参数请序列化为字符串如上所述放置于 `Query` 中，参数格式如下：

```javascript
{
    "TimeStamp": 1555672740000,     // UNIX 时间戳（毫秒）
    "Data": {
        // 数据
    }
}
```

**注意：参数中的 `TimeStamp` 为必选参数，JSONAPI-Nukkit(X) 将对此参数和服务器当前时间进行比较，若时间相差大于等于 2 分钟，则会拒绝这次请求并返回 403 Forbidden 信息。**

### API 鉴权

为了 API 调用安全，请将鉴权信息放置于请求 `Header` 的 `X-JsonApi-Authentication`，鉴权信息生成方式：

```
sha256(userName + api_url.toLowerCase() + password)
```

例如在配置文件（将在下文介绍）中设置的 UserName 为 `exampleUser`，Password 为 `testpass`，需要调用的 API 地址为 `/api/Server/GetServerInfo` ，则鉴权信息为：

> sha256( "exampleUser" + "/api/Server/GetServerInfo".toLowerCase() + "testpass" )

# 配置文件
JSONAPI-Nukkit(X) 的配置文件位于服务器的 `plugins/JSONAPI` 文件夹下，初次启动插件会自动生成配置文件，您可以根据需要自行修改。配置文件的格式如下：

```YAML
Server:
  IP: 0.0.0.0
  Authentication:
    Password: password
    UserName: root
  HttpPort: 19133
  WsPort: 19134

EventListener:
  IsEnable: true
  EventList:
    - ServerCommand
    - PlayerChat

DebugMode: true
```

## 配置文件说明

- Server.Authentication 是 API 鉴权所需要的用户名和密码。
- Server.IP 是配置 WebSocket 服务器监听的 IP 地址。
- Server.HttpPort 是 API 服务器监听的端口，**请不要与 Nukkit(X) 本身的端口冲突**。
- Server.WsPort 是 W1ebSocket 服务器监听的端口，**请不要与 Nukkit(X) 本身的端口冲突**。
- EventListener.IsEnable 配置事件通知的 WebSocket 服务是否启用。
- EventListener.EventList 如果 `EventListener.IsEnable` 为 true ，则这个列表下的事件会被 JSONAPI-Nukkit(X) 监听，并且通过 WebSocket 发送实时通知。
- DebugMode 配置是否启动 Debug 模式，若为 true 则会在控制台输出调试信息并且调用 API 时无需鉴权信息。

# 下载

你可以查阅 [release 页面](release) 下载最新的构建版本。

# 开发相关

### 已知问题
- 在调用 `/api/Server/ExecuteCommand` 以运行一条命令时，控制台将会抛出一个异常，原因是不在主线程执行命令时会抛出错误，但是命令还是会正常执行。等待 Nukkit(X)官方修复此问题。

# 更新日志
本项目遵从 [Angular Style Commit Message Conventions](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)，更新日志由 `conventional-changelog` 自动生成。完整日志请点击 [CHANGELOG.md](./CHANGELOG.md)。

# License
MIT License

Copyright (c) 2019 Tuisku Wood

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.