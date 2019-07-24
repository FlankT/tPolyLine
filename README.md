# tPolyLine

**自定义动态双折线，股票金融**

# 示例：
      <com.cd.t.polyline.PolyLine
        android:id="@+id/polyline"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/color1"
        android:visibility="visible" />

# 依赖方式：

Add it in your root build.gradle at the end of repositories:

    allprojects {

	  repositories {
	  
			...
			
			maven { url 'https://jitpack.io' }			
		  }	
	}
	
Add the dependency：

     dependencies {

	        implementation 'com.github.FlankT:tPolyLine:v1.0.0'
		
	}
	
