## 系统简介

![03](https://s2.loli.net/2022/06/12/VPBM7X5KuWRL6Fn.png)

## 开发环境

![04](https://s2.loli.net/2022/06/12/ok3cImzHadP6hDO.png)

## 系统功能
![05](https://s2.loli.net/2022/06/12/OJXCaxDevntPW85.png)

## 开发框架

![06](https://s2.loli.net/2022/06/12/qfFhwX4DUKGoLWA.png)

## 架构设计

![07](https://s2.loli.net/2022/06/12/2qgB9VKntQfLb3G.png)

## 系统功能说明
### 基础功能
#### 注册
用户进入账号注册页面进行账号的注册时，需填写账号、密码、二次确认密码和邮箱。
![17453](https://s2.loli.net/2022/06/12/sHiXhmW68v7cON2.png)

![17462](https://s2.loli.net/2022/06/12/5kwed7TioP4raNZ.png)

#### 登录/登出
用户登录系统时，需要填写账号、密码和验证码，用户可勾选“记住我”来延长保持账号登录状态的时间。

![17549](https://s2.loli.net/2022/06/12/Yzgsx7RjohruD9p.png)

![17556](https://s2.loli.net/2022/06/12/YH1sntgEPyFNib4.png)

#### 重置密码

用户通过登录界面的“找回密码”按钮进入重置密码界面。

![17609](https://s2.loli.net/2022/06/12/mFVpuiHTXO4SgwE.png)

#### 账号设置
用户从功能栏进入账号设置，可修改账号头像和密码。

![17664](https://s2.loli.net/2022/06/12/XIWFhSr6naA7o3m.png)

### 社区功能
#### 文章功能
文章功能主要包括文章的浏览和文章的发布，在文章的详情页面，右侧为文章内容的目录，点击可快速跳转。在文章详情页面可对文章进行评论、点赞、收藏等操作。用户通过Markdown在线编辑器进行文章的编写，左侧为文档源内容，右侧为实时渲染后的排版样式，在文章撰写完成要发布时，需要选择文章的所属分类，也可以为文章添加多个标签，方便其他用户的检索查看。

![17917](https://s2.loli.net/2022/06/12/GO1goliL8qSQtfW.png)

![17937](https://s2.loli.net/2022/06/12/W5LPgdVyZScxEIs.png)

![17948](https://s2.loli.net/2022/06/12/IGF7KpsQ9SmCEHi.png)

#### 评论功能
在文章详情页面底部的评论区，用户可查看该文章的相关评论，评论分为三种情况，分别是用户对文章的评论、用户对其他用户评论的回复和用户对其他用户的回复进行回复。

![18057](https://s2.loli.net/2022/06/12/BADXOTCGMw6f7Z8.png)

#### 收藏功能
用户在文章详情页面，可以通过收藏按钮对该篇文章进行收藏或放弃收藏。在系统首页的“收藏”板块可以查看自己所收藏的文章列表。

![18147](https://s2.loli.net/2022/06/12/kVymTJZFfhuXq83.png)

#### 个人主页
用户可以通过用户头像进入该用户的个人主页或通过功能栏的“个人主页”按钮进入自己的个人主页，在个人主页可以查看该用户的账号相关信息、所关注用户的数量、关注者的数量、获得赞的数量、所发布的文章和所评论的文章。

![18279](https://s2.loli.net/2022/06/12/Mdi4Huoc7jBspTf.png)

#### 关注功能
用户通过头像或者用户名进入其他用户的主页后，可以通过关注按钮关注或取消关注该用户，也可以查看该用户关注的人、关注该用户的人和关注时间等内容，也可在此页面快捷地对列表中的用户进行关注或取消关注。当用户有了关注的对象后，在系统首页的“关注”板块可以查看自己所关注的用户所发布的文章。

![18448](https://s2.loli.net/2022/06/12/sIMBwDXa2kcvEoG.png)

#### 私信功能
用户通过系统功能栏的“消息”按钮使用私信功能。用户可以查看私信列表和私信详情，用户可以在私信列表通过“发私信”按钮发起新的对话，或是点击私信列表中的某一个会话进入私信详情页面，通过“回复”按钮给该用户发送私信。

![18599](https://s2.loli.net/2022/06/12/vgnCe1Mc2adJsEx.png)

![18608](https://s2.loli.net/2022/06/12/UL5vz1RVYIynA87.png)

#### 系统通知
用户在功能栏的“消息”按钮旁可以看到所有的未读消息数量，点击进入系统通知列表，再次点击可进入某一类型的通知详情，可快速跳转到相应的文章进行查看。![18728](https://s2.loli.net/2022/06/12/I8MPXjvp7m961sG.png)

![18737](https://s2.loli.net/2022/06/12/kQAbofYz3RtgBey.png)

#### 搜索功能
用户在系统功能栏的搜索框内输入想要搜索的内容，所输入的搜索字符串会被系统分词后进行文章标题或文章内容的关键字匹配，搜索结果页面会对匹配到的关键字进行特殊标记。

![18844](https://s2.loli.net/2022/06/12/f3OYtNoeg4I2wuM.png)

#### 网站数据统计
管理员登录系统后，从功能栏访问网站数据统计页面，可以查询系统某个时间段内的独立访客数和活跃用户数，查看系统运行期间的访问数据曲线图。

![18944](https://s2.loli.net/2022/06/12/ZUu1gOHbQDBSdvT.png)

### 后台管理功能
#### 分类管理
用户发布文章时，需要选文章的分类。管理员可通过该功能对文章的分类进行管理，直观地查看所有分类名称、引用数量和状态，可以查询、新建、编辑、停用、启用和删除文章的分类。

![19064](https://s2.loli.net/2022/06/12/ByVwDEHcCPA9gJY.png)

#### 标签管理
用户发布文章时可给文章添加多个标签，所属分组相同的标签为相关标签。标签管理功能可根据标签名称、所属分组、描述或状态对标签进行查找、新建、停用、启用、编辑和删除标签信息。

![19176](https://s2.loli.net/2022/06/12/6bB3sJYRyjCUPEO.png)

#### 文章管理
管理员可以根据文章的类型或状态对文章进行筛选，通过文章标题对文章进行搜索。按照文章热度排序进行查看，置顶或取消置顶文章，对文章的状态进行加精、拉黑、恢复等操作。

![19284](https://s2.loli.net/2022/06/12/fskt7OQKmFyMl1Y.png)

#### 用户管理
用户管理功能只有超级管理员有使用权限，他可以根据用户名、用户邮箱、用户类型、用户状态进行用户的搜索，拉黑或激活用户，给用户分配管理员权限。

![19383](https://s2.loli.net/2022/06/12/riTmWn3tukZleB2.png)
