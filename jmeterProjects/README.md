# Jmeter 使用总结

> 之前写过关于soapui的使用总结，但是在测试工作中，只是进行业务层面上的接口测试是完全不能满足云服务提供商的要求。因为提到云服务，大家们肯定会想到‘高并发’、‘灾备’、‘异步’、‘阻塞’等名词，而且当这家云服务公司还具备大数据分析方面的服务时，单一的业务数据就显得那么弱不禁风啦！这个时候我们需要创建出多样的、复杂的，但还有规律可寻的大量数据。虽然soapui以及postman这些接口工具也都具备重复调接口进行测试的功能，但是灵活性都不是很好，那么我就试试看用开源的Jmeter来一次测试。
**以下内容都是基于jmeter-3.1版本**

![IndexPage](https://github.com/zackgq2009/TestDocuments/blob/master/jmeterProjects/jmeterPictures/jmeterIndexPage.png)

## Jmeter 使用环境

Jmeter一直是一款非常优秀的压力测试工具，可以设置线程数、并发数、请求间的间隔时间以及整个线程组的循环次数...等等参数设置，可以让我们很轻松的实现几百上千的并发数据，这些数据又恰好符合大数据分析的需求。在这种环境前提下，我就在测试压力的同时，创建出以供数据分析用的基础数据。

## Jmeter 使用版本

用Jmeter已经有两年多了，从最初的2.13版本，到后来相对稳定的3.0版本，以及最新的3.1版本，这些版本在UI方面基本没有什么大的改动，我在写这篇文章时3.1版本已经正式发布，那我就拿这个最新的版本进行演示吧~~~[官网地址](http://jmeter.apache.org/)

## Jmeter 官方文档

官方文档详细讲述了GUI模式下的操作，教会用户如果去创建一个压力测试的线程组(Thread Group)，如何在线程组下创建不同的控制器(Controllers)、采样器(Samplers)，以及如何正确的组合使用定时器(timers), 监听器(listeners), 断言(assertions)。

## Jmeter 工作结构

### 测试计划（Test Plan）

测试计划就是项目的‘根’，其他所有的对象都是建立在测试计划之上的。在‘测试计划’中可以为整个测试过程设置全局变量，例如：

```
 For example the variable `SERVER` could be defined as `www.example.com`, and the rest of the test plan could refer to it as `${SERVER}`. This simplifies changing the name later.
```

但是测试计划中的全局变量有可能会被用户添加的`User Defined Variables`中的变量所覆盖掉，所有变量的值都是‘就新原则’，如果变量名一样的情况下，均以最后一次定义的值为准。

测试计划中有一项配置`Run Thread Groups consecutively(run groups one at a time)`，此项配置只针对线程组的，用来只是线程组是串行运行还是并行运行。

在测试计划中可以根据测试内容的特殊化，来添加特定的JAR包，还可以通过在`jmeter.properties`中编辑来添加依赖的类。

```
JMeter properties also provides an entry for loading additional classpaths. In `jmeter.properties`, edit `"user.classpath"` or `"plugin_dependency_paths"` to include additional libraries. See `JMeter's Classpath` and `Configuring JMeter` for details.
```

### 线程组（Thread Group）

Thread group elements are the beginning points of any test plan. All controllers and samplers must be under a thread group. （我们的压力测试必须先创建一个线程组，其他所有的控制器，采样器都必须在线程组的下边）

#### 线程组中的属性设置

线程组中设置三个参数：

Set the number of threads(线程数)
Set the ramp-up period（线程增加的时间长度，例如：设置了100个线程，线程从1到100需要多久）
Set the number of times to execute the test（运行圈数）
Version 1.9 introduces a test run scheduler. （在1.9版本之后，我们可以设置一个定时的线程组，其中包括线程组的开始时间，结束时间，持续时间以及开始之后持续多长时间）

![ThreadGroupPage](https://github.com/zackgq2009/TestDocuments/blob/master/jmeterProjects/jmeterPictures/ThreadGroupPage.png)

```
我们可以通过GUI页面中鼠标右键点击线程组，来选择三种不同的运行模式，一种是直接`start`，第二种是`Start no pauses`，第三种就是`Validate`（一种验证模式），这种验证模式的参数设置，需要用户在`user.properties`中找到相应的参数进行设置。
```

### 控制器(Controllers)

JMeter has two types of Controllers: Samplers and Logical Controllers. These drive the processing of a test. (两种控制器，一种就是采样器，另一种就是逻辑控制器)

Logical Controllers let you customize the logic that JMeter uses to decide when to send requests. （我们在线程组下边可以创建各种逻辑控制器，如果想让控制器控制采样器的发送，我们就需要把这些采样器创建在逻辑控制器的下边）

Samplers tell JMeter to send requests to a server and wait for a response. They are processed in the order they appear in the tree. Controllers can be used to modify the number of repetitions of a sampler. (所有采样器的运行顺序都是在jmeter中线程组树下的排列顺序，控制器可以修改采样器的重复数量)

### 采样器（Samplers）

![Samplers](https://github.com/zackgq2009/TestDocuments/blob/master/jmeterProjects/jmeterPictures/Samplers.png)

```JMeter samplers include:
FTP Request
HTTP Request
JDBC Request
Java object request
LDAP Request
SOAP/XML-RPC Request
WebService (SOAP) Request
```

### 逻辑控制器（Logical Controllers）

![LogicalControllers](https://github.com/zackgq2009/TestDocuments/blob/master/jmeterProjects/jmeterPictures/LogicalControllers.png)

Logic Controllers let you customize the logic that JMeter uses to decide when to send requests. Logic Controllers can change the order of requests coming from their child elements. They can modify the requests themselves, cause JMeter to repeat requests, etc. (逻辑控制器是用来定制我们请求的发送逻辑，可以更改请求的顺序。我们可以在逻辑控制器的子目录下创建采样器，这样的话，这些控制器就直接控制这些子采样器)

### 一个完整的Jmeter项目结构

![jmeterTree](https://github.com/zackgq2009/TestDocuments/blob/master/jmeterProjects/jmeterPictures/jmeterTree.png)

一个包括采样器，以及不同逻辑控制器的样例：（其中所有层次结构也应与jmeter中保持一致）  
- Test Plan
    - setUp Thread Group
    - Thread Group
        - Once Only Controller
            - Login Request (an HTTP Request)
        - Load Search Page (HTTP Sampler)
        - Interleave Controller
            - Search "A" (HTTP Sampler)
            - Search "B" (HTTP Sampler)
            - HTTP default request (Configuration Element)
        - HTTP default request (Configuration Element)
        - Cookie Manager (Configuration Element)
    - testDown Thread Group
    - WorkBench

The Thread Group has a built-in Logic Controller.
（线程组里边有一个内置的逻辑控制器）  
比如上边的例子：`Load Search Page(HTTP Sampler)`这个采样器是直接加载在线程组下边的，它与其他的逻辑控制器平级，并且不受其他逻辑控制器的影响，它自身的运行只收到线程组内置控制器的影响。

### WorkBench

```
The WorkBench simply provides a place to temporarily store test elements while not in use, for copy/paste purposes, or any other purpose you desire. When you save your test plan, WorkBench items are not saved with it by default unless you check "Save Workbench" option. Your WorkBench can be saved independently, if you like (right-click on WorkBench and choose Save).
```

**工作台** 是测试计划下默认就存在的一个元素，他存在的意义就是临时帮你保存那些你一时用不上的对象，包括控制器、采样器、监听器等等。在保存测试计划的时候，默认不会把工作台也一同保存下来，我们需要勾选工作台下方的`Save WorkBench`来进行保存。而且工作台与线程组唯一的区别就是工作台下可以添加三个“非测试元件”，其中包括`HTTP(S) Test Script Recorder`、`HTTP Mirror Server`、`Property Display`

### Debug Sampler && Debug PostProcessor

```
The Debug Sampler generates a sample containing the values of all JMeter variables and/or properties.  
The Debug PostProcessor creates a subSample with the details of the previous Sampler properties, JMeter variables, properties and/or System Properties.  
The values can be seen in the [View Results Tree](http://jmeter.apache.org/usermanual/component_reference.html#View_Results_Tree) Listener Response Data pane.
```

说实话，我还不是很了解这两个对象的作用，主要是工作中还没有用到这块。

### Test Fragment

```
The Test Fragment is used in conjunction with the Include Controller and **Module Controller**.
```

我们在使用jmeter中需要对`Module Controller`要有一定的了解，他会很好的帮我们整理采样器。

### setUp Thread Group && tearDown Thread Group

**线程组** 其实是有三种的，一种就是我们最常用的普通线程组，我们在测试计划下创建线程组才能为其创建其他的采样器、控制器以及监听器等。然而`setUp`&`tearDown`其实跟正常的线程组没有什么区别，唯一的区别就是`setUp`是在所有的线程组之前运行，`tearDown`则是在所有的线程组之后运行（`tearDown Thread Groups`还有个特殊的开关，在测试计划页面中。并且其他线程组在运行过程中‘被停止’了之后，不管是否勾选了开关，`tearDown`都不会运行，只有其他线程组正常运行完之后，才能根据开关进行tearDown的运行）。


## Jmeter 多种模式的运行方式

./bin目录下包含了多种运行脚本，To run JMeter, run the jmeter.bat (for Windows) or jmeter (for Unix) file. These files are found in the bin directory. After a short time, the JMeter GUI should appear.（bat是针对Windows环境的，jmeter则是针对Unix环境的，正确运行这些脚本之后，JMeter GUI过会就会自动打开）

Environment variables: `JVM_ARGS - optional java args, e.g. -Dprop=val`
windows系统中:`set JVM_ARGS="-Xms1024m -Xmx1024m -Dpropname=propvalue" jmeter -t test.jmx …`
Unix系统中:`JVM_ARGS="-Xms512m -Xmx512m" jmeter etc.`

```
_    ____   _    ____ _   _ _____       _ __  __ _____ _____ _____ ____
/ \  |  _ \ / \  / ___| | | | ____|     | |  \/  | ____|_   _| ____|  _ \
/ _ \ | |_) / _ \| |   | |_| |  _|    _  | | |\/| |  _|   | | |  _| | |_) |
/ ___ \|  __/ ___ \ |___|  _  | |___  | |_| | |  | | |___  | | | |___|  _ <
/_/   \_\_| /_/   \_\____|_| |_|_____|  \___/|_|  |_|_____| |_| |_____|_| \_\ 3.1 r1770033

Copyright (c) 1999-2016 The Apache Software Foundation


To list all command line options, open a command prompt and type:

jmeter.bat(Windows)/jmeter.sh(Linux) -?

--------------------------------------------------

To run Apache JMeter in GUI mode, open a command prompt and type:

jmeter.bat(Windows)/jmeter.sh(Linux) [-p property-file]

--------------------------------------------------

To run Apache JMeter in NON_GUI mode:
Open a command prompt (or Unix shell) and type:

jmeter.bat(Windows)/jmeter.sh(Linux) -n -t test-file [-p property-file] [-l results-file] [-j log-file]

--------------------------------------------------

To run Apache JMeter in NON_GUI mode and generate a report at end :
Open a command prompt (or Unix shell) and type:

jmeter.bat(Windows)/jmeter.sh(Linux) -n -t test-file [-p property-file] [-l results-file] [-j log-file] -e -o [Path to output folder]

--------------------------------------------------
To generate a Report from existing CSV file:
Open a command prompt (or Unix shell) and type:

jmeter.bat(Windows)/jmeter.sh(Linux) -g [csv results file] -o [path to output folder (empty or not existing)]

--------------------------------------------------

To tell Apache JMeter to use a proxy server:
Open a command prompt and type:

jmeter.bat(Windows)/jmeter.sh(Linux) -H [your.proxy.server] -P [your proxy server port]

---------------------------------------------------

To run Apache JMeter in server mode:
Open a command prompt and type:

jmeter-server.bat(Windows)/jmeter-server(Linux)

---------------------------------------------------
```

![MAC run Jmeter](https://github.com/zackgq2009/TestDocuments/blob/master/jmeterProjects/jmeterPictures/startJmeter.png)

### Windows run Jmeter（因为windows上没有太多使用经验，所以在这边只好列出来相应指令说明）

`**GUI mode should only be used for creating the test script, NON GUI mode must be used for load testing**`

There are some additional scripts in the bin directory that you may find useful. Windows script files (the .CMD files require Win2K or later):

`jmeter.bat`  
    run JMeter (in GUI mode by default)

`jmeterw.cmd`  
    run JMeter without the windows shell console (in GUI mode by default)

`jmeter-n.cmd`  
    drop a JMX file on this to run a non-GUI test

`jmeter-n-r.cmd`  
    drop a JMX file on this to run a non-GUI test remotely

`jmeter-t.cmd`  
    drop a JMX file on this to load it in GUI mode

`jmeter-server.bat`  
    start JMeter in server mode

`mirror-server.cmd`  
    runs the JMeter Mirror Server in non-GUI mode

`shutdown.cmd`  
    Run the Shutdown client to stop a non-GUI instance gracefully

`stoptest.cmd`  
Run the Shutdown client to stop a non-GUI instance abruptly

`The special name LAST can be used with jmeter-n.cmd, jmeter-t.cmd and jmeter-n-r.cmd and means the last test plan that was run interactively.`

The environment variable JVM_ARGS can be used to override JVM settings in the jmeter.bat script. For example:

```
set JVM_ARGS="-Xms1024m -Xmx1024m -Dpropname=propvalue"
jmeter -t test.jmx …
```

### run Jmeter in GUI Mode

### run Jmeter in Non-GUI Mode

### SSL Manager

用户可以在`Option -> SSL Manager`中选择需要的SSL证书，证书应该是`.p12`格式的`PKCS12`文件。并且`JSSE`库会要求输入密码。

同样的，`SSL`证书也可以在`system.properties`中相应配置。并且正确的秘钥必须是第一个。

## Jmeter中的使用技巧--总结
