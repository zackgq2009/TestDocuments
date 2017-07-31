# saucelabsTestCases
### 1.首先是测试脚本如何运行出来，让sauce labs帮我们直接运行自动化测试？首先我们的测试代码依赖selenium 以及appium相应的库，我们下载好相应的库，并且在脚本代码中使用DesiredCapabilities来定义我们的参数（这些参数是来配置sauce labs用哪个系统、哪个浏览器、哪个版本、哪个平台、哪个APP以及为生成的session生成一个名字），然后使用RemoteWebDriver对象new出来一个driver，这个driver就是用来测试的
### 2.我们只要写好我们sauce labs的用户名、accessKey就可以啦
### 3.我们的URL最好是HTTP的，发现写成HTTPS貌似跑不通（也有可能是我们需要配置什么证书、认证之类的）
### 4.貌似测试APP的时候，我们需要把我们的应用上传到他们的仓库中（https://wiki.saucelabs.com/display/DOCS/Uploading+Mobile+Applications+for+Testing）
