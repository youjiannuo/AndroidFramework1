# AndroidFramework1
#内部写了开发文档
Android框架说明文档
1.	前言
这个框架加快了一倍的开发速度，减少了70%以前的代码量，减少了百分之90%以前的bug，要达到这样的效果，前提是需要熟悉这个框架。听起来是不是在扯淡呢？不是的，现在已经可以的！不过需要注意的是，这个框架不一定适合所有的项目，因为这个框架是我个人总结出来的，需要不断升级，但是里面的设计思想绝对值得学习的。
该框架采用的是响应式编程的思想，API被绑定到UI上面去。如何来减少代码量呢，我就封装了常用几个控件ListView,TextView,ImageView,EditText和LinearLayout，封装后的名字分别是YNListView,YNTextView,YNImageView,YNEditText和YNLinearLayout。以后在绘制布局的时候使用这几个控件就好了，然后我在attrs自定义了非常多的属性，这些自定义属性。
2.	完成功能介绍
该框架都是使用封装好的控件YNListView,YNTextView,
YNImageView,YNEditText和YNLinearLayou来绘制布局，然后自定义了非常多的属性，然后我们可以在这些属性上面来配置发送HTTP请求，然后配置一些参数可以把从服务器获取的参数设置到UI上面，例如，可以通过配置来设置ListView的加载更多，下拉刷新，将数据设置到ListView上面去。 
3.框架数据配置
1）.请看到values里面有一个application_configuration_parameters这个文件里面写了一些基本信息的配置，主要是解析服务器返回给我们的json格式。里面写好注释，请逐个配置。
2）.请配置build.gradle(Module:app)里面的参数，
其中release是正式环境，debug是测试环境。
4.使用方式
可以查看下value里面有一个文件叫attrs.xml，里面写了几个控件添加的属性。
从一个简单的获取数据然后把
1)先在你的项目里面的value新建一个http.xml，这里主要是写api。下面开始api格式
<string-array name="yn_get_info">
    <!--发送的url，这里的url，没有写全主要是在gradle里面配置好了郁闷-->
    <item>url:wxmp.sssh.admin/api/app/testList</item>
    <!--key 发送给服务器的key , 将会有和他相对应的value,主要a3这个key可以在代码里面去配置-->
    <item>key:a1,a2,a3</item>
    <item>value:1,2</item>
    <!--是否弹出toast ,如果是false，不弹出-->
    <item>isToast:false</item>
    <!--是否添加缓存,只要添加了这个参数就有缓存，缓存服务器返回的数据-->
    <item>cache:1</item>
    <!--这个主要是把参数添加到缓存的标示，例如我们需要缓存一个API的数据，但是API的url相同，那么每次缓存就会出问题了，这个时候把参数添加上去，就不会出现问题-->
    <item>cacheaddparam:true</item>
    <!--发送网络清楚的方式，这里面只写了几个方法网络请求方法，post,get,head,put,delete-->
    <item>http:post</item>
</string-array>
可以根据自己的需求选择参数
2）可以在文件value文件夹下面新建一个文件activity.xml然后可以配置如下参数
<string-array name="hfh_function_new_guide">
    <!--需要跳转的activity,前面项目包名已经配置了，只需要写出后一半-->
    <item>class:.AndroidFunctionChooseStockActivity</item>
	<!--需要传送给那个Activity的key-->
    <item>key:1,level</item>
	<!--相对应的是value , 这个可以在多个地方配置-->
    <item>value:0</item>
</string-array>

3.	配置好了一个参数，我们可以写布局文件了，布局文件里面配置一些参数，由于里面属性很多了，可以进行逐个介绍
1)	YNListView
app: list_http 这个是发送API请求，配置的是http.xml里面的数据
app:layout_value 这个是配置listView每一个item 的layout
app:list_load_more 如果需要加载更多，请配置每一页加载的数量，还需要在http.xml里面添加<item>key:page</item>对应的page,请把page加载到第一个。
app:layout_title 标题的布局
app:head_value 	listView的head布局
app:foot_value 	listView的foot布局
app:set_data_name 这个是json格式对应的key
app: onItemBackground 每一item layout的背景资源
app:onItemLineHeight	这个是每一个Item 的高度
app:onItemLineColor 分割新的颜色
app: list_show_all_view 这个是将listView给全部撑开，主要是应用于ListVew外面包括了一个滚动条
2）YNLinearLayout
app:onClickValue 点击事件，这里面可以发送http请求，同事也是可以跳转
app:onClick 这个是点击事件触发的动作，
其中有http,这个是发送网络请求，所以在onClickValue赋值的是对应http.xml里面的
activity，点击跳转Activity.
app:onClickKey 主要是发送http或者跳转到activity,需要传送的给http获取跳转Activity，对应的value，例如，在当前activity的Intent里面携带了多对应的数据，如a=12,b=32,那么我们在发送HTTP获取跳转Activity想把这个两个数据也传送出去，那么这个设置就是这样写app:onClickKey=”a,b”。

