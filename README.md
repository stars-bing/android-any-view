# Android-AnyView

#### 介绍
安卓常用的一些自定义View

#### 使用

```groovy
implementation("com.github.stars-bing:android-any-view:v1.0.3")
```

#### 常用的自定义View

1.  RoundImageView：圆角、圆形的ImageView
2.  VerificationCodeInput：常见的弹框密码输入框
3.  RingProgressBar: 圆形进度条
4.  AutoBoldTextView: 自定义文字粗细大小
5.  CustomLayout: 自定义的LinerLayout布局，以第二个View跟在第一个TextView后面
6.  OutLinedTextView: 描边文字
7.  FlowLayoutManager: 流式布局
8.  ChatInputView: 处理输入在粘贴、键盘快捷粘贴拦截
9.  TriangleView：自定义三角形
10. DashedLineView：自定义虚线


#### 使用说明
**DashedLineView-虚线**
```xml
    <!-- 虚线的颜色 -->
    <attr name="line_color" format="color"/>
    <!-- 虚线的宽度 -->
    <attr name="dash_length" format="dimension"/>
    <!-- 虚线的间距 -->
    <attr name="gap_length" format="dimension"/>
    <!-- 虚线的高度 -->
    <attr name="line_size" format="dimension"/>
    <!-- 虚线的方向 -->
    <attr name="line_type" format="enum">
        <enum name="horizontal" value="1" />
        <enum name="vertical" value="2" />
    </attr>
```

**TriangleView-三角形**
```xml
    <!-- 三角形的颜色 -->
    <attr name="bg_color" format="color"/>
    <!-- 为正三角还是倒三角 -->
    <attr name="triangle_type" format="enum">
        <enum name="reversal" value="2" />
        <enum name="square" value="1" />
    </attr>
```