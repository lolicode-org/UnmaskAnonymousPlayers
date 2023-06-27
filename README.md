为白名单内的ip发出的查询请求提供完整的玩家列表，即使服务端禁用了查询或者启用了隐藏玩家列表也是如此。

这对于希望能够通过查询接口获取服务器内玩家信息，但是不希望将玩家信息暴露在公网的服务器可能有用。

此外，该mod还附加了两个功能，分别是提供完整的玩家列表（原版最大12个）和不打乱玩家列表（原版会打乱），以便于服务器管理。这两个功能默认关闭，需要在配置文件中打开。

### 使用
1. 下载mod，放入**服务器的**mods目录内
2. 启动服务器，等待mod生成配置文件
3. 编辑配置文件（`config/unmaskanonymousplayers.json`），将需要的ip地址（或者ip地址段）添加到`whitelist`数组内
4. 使用`/uap reload`命令重载配置文件

### 配置文件
```json5
{
  "whitelist": [],  // 白名单，支持ip地址段。如：["127.0.0.1", "192.168.0.0/16"]
  "showFullPlayerList": false,  // 是否显示完整的玩家列表。注意当玩家数量过大时，可能带来无法预期的问题
  "shufflePlayerList": true  // 是否打乱玩家列表，默认为原版行为（打乱）
}
```

---

Provide complete player list for query requests from whitelisted ip addresses, even if the server has query disabled or hide player list enabled.

This may be useful for servers that want to provide player information through the query interface, but do not want to expose player information to the public network.

This mod provide two additional features:
- Provide full player list (in vanilla server, the maximum is 12)
- Do not shuffle player list (vanilla server will shuffle the list)

These two features are disabled by default and need to be enabled in the configuration file.

### Usage
1. Download the mod and put it in the **server's** mods directory
2. Start the server and wait for the mod to generate the configuration file
3. Edit the configuration file (`config/unmaskanonymousplayers.json`) and add the required ip addresses (or ip-cidr) to the `whitelist` array
4. Use the `/uap reload` command to reload the configuration file

### Configuration file
```json5
{
  "whitelist": [],  // whitelist, support ip-cidr. e.g. ["127.0.0.1", "192.168.0.0/16"]
  "showFullPlayerList": false,  // whether to show the full player list. Note that when the number of players is too large, it may cause unexpected problems
  "shufflePlayerList": true  // whether to shuffle the player list, default to vanilla behavior (shuffle)
}
```
---
### License
```text
Copyright (c) 2023 KoishiMoe

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

```