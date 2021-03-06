[![](https://jitpack.io/v/sam38124/JzCrashHandler.svg)](https://jitpack.io/#sam38124/JzCrashHandler)
[![Platform](https://img.shields.io/badge/平台-%20Android%20-brightgreen.svg)](https://github.com/sam38124)
[![characteristic](https://img.shields.io/badge/特點-%20輕量級%20%7C%20簡單易用%20%20%7C%20穩定%20-brightgreen.svg)](https://github.com/sam38124)
# JzCrashHandler
#### <br> 最近請測試工程師做黑箱測試時，偶爾會遇到崩潰的問題，因為發生的機率不高，很難模擬出崩潰的原因，雖然也可以使用firebase進行除錯，但是也要進行一些前置動作，所以自行開發一個崩潰處理框架，儲存崩潰紀錄於本地，或者推播至Firebase，整體代碼十分簡單，只要於Application添加一行程式碼即可

#### 顯示本地崩潰紀錄

<img src="https://github.com/sam38124/JzCrashHandler/blob/master/IMG_mdny8p.gif" width = "200"  alt="i5" /> 

#### 搭配[JzCustomDebug](https://github.com/sam38124/JzCustomDebug)線上除錯系統

<img src="https://github.com/sam38124/JzCustomDebug/blob/master/i1.png" width = "200"  alt="i1" />  <img src="https://github.com/sam38124/JzCustomDebug/blob/master/i2.png" width = "200"  alt="i2" />  <img src="https://github.com/sam38124/JzCustomDebug/blob/master/i3.png" width = "200"  alt="i3" />

## 目錄
* [如何導入到專案](#Import)
* [快速使用](#Use)
* [關於我](#About)

<a name="Import"></a>
## 如何導入到項目
> 支持jcenter。 <br/>

### jcenter導入方式
在app專案包的build.gradle中添加
```kotlin
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

在需要用到這個庫的module中的build.gradle中的dependencies中加入
```kotlin
dependencies {
implementation 'com.github.sam38124:JzCrashHandler:2.3'
}
```
<a name="Use"></a>
## 快速使用

### 於Application中添加你的崩潰處理方式 

#### 1.崩潰後儲存崩潰紀錄並且自動重啟app
```kotlin
CrashHandle.newInstance(this,MainActivity::class.java).setUp(CrashHandle.RESTART)
```
#### 2.崩潰後顯示Log紀錄檔
```kotlin
CrashHandle.newInstance(this,MainActivity::class.java).setUp(CrashHandle.SHOW_CRASH_MESSAGE)
```
#### 3.崩潰後上傳Log紀錄檔至[JzCustomDebug](https://github.com/sam38124/JzCustomDebug)線上除錯系統
```kotlin
CrashHandle.newInstance(this,MainActivity::class.java).setUp(CrashHandle.UPLOAD_CRASH_MESSAGE)
```
### 其他 
```kotlin
CrashHandle.getInstance().readLog()//讀取本地崩潰紀錄

CrashHandle.getInstance().deleteRecord()//刪除本地崩潰紀錄
```
<a name="About"></a>
### 關於我
橙的電子Android and Ios Developer

*line:sam38124

*gmail:sam38124@gmail.com
