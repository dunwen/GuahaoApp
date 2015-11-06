# GuahaoApp
模拟登录挂号app（www.guahao.com）， 实现预约挂号，登录，绕过验证码，后台监听不可预约的挂号状态转换成可预约状态时自动挂号

当时遇到的问题:
  1. 登录的时候找不到保持持续登录的session 
  解决： 用chrome到网站去一个一个地去删除cookie 删除那个session（cookie）后要求重新登录的那个就是 保持持续登录的cookie；
  （_ci_ __i__ 就是这两个cookie保持持续登录，其它为存贮用户数据，发送http请求的时候把cookie写入头发送即可）
  
  2.找到保持持续登录的cookie后 在本地模拟登录的时候获取不了相应的cookie
  解决： 后来发现登录成功后的http状态码是304被重定向了（也是因为这个粗心没有发现导致多了半天的工作量），后来设置
   HttpURLConnection.setInstanceFollowRedirects(false) 就获取到重定向前的cookie
   
   3.爬取网页数据
  解决 ：jsoup 方便快捷。。。可写入cookie 可爬取数据（因为是临时写的，并没有去保证网页改变后是否还能获取相应的数据， 而这个
  项目因为乙方违约的原因我们并不会继续去维护，所以才会公开代码）
  
  4.想到了再写
  
  
