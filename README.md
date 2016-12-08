# fy

![](https://github.com/binaryer/fy/blob/master/fy.gif)  

minimal command line [ZHCN->ENG / ENG->ZHCN] translation tool, using [fanyi.baidu.com](http://fanyi.baidu.com)  

极简的命令行[中英/英中]翻译工具, 使用 [fanyi.baidu.com](http://fanyi.baidu.com) 接口  

开发框架: 
+ commons-io-2.5.jar
+ groovy-json-2.4.7.jar
+ jodd-http-3.7.1.jar

## Usage
```bash
java -jar fy.jar i love you
java -jar fy.jar 我爱你

#alias
alias fy='java -jar /path/to/fy.jar'
fy i love you

#支持管道输入	
echo hello | fy -
cat file_to_trans.txt | fy -
fy - < file_to_trans.txt
```

## Author

林春宇@深圳  
chunyu_lin@163.com

## Download
[https://github.com/binaryer/fy/releases](https://github.com/binaryer/fy/releases)
