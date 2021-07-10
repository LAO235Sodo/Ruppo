@Retention
1、Retention英文意思有保留、保持的意思，它表示注解存在阶段是
保留在源码（编译期），
字节码（类加载），
运行期（JVM中运行）。
在@Retention注解中使用枚举RetentionPolicy来表示注解保留时期
@Retention(RetentionPolicy.SOURCE)，注解仅存在于源码中，在class字节码文件中不包含;
@Retention(RetentionPolicy.CLASS)， 默认的保留策略，注解会在class字节码文件中存在，但运行时无法获得
@Retention(RetentionPolicy.RUNTIME)， 注解会在class字节码文件中存在，在运行时可以通过反射获取到


@Target
Target的英文意思是目标，这也很容易理解，
使用@Target元注解表示我们的注解作用的范围就比较具体了，可以是类，方法，方法参数变量等，同样也是通过枚举类ElementType表达作用类型
@Target(ElementType.TYPE) 作用接口、类、枚举、注解
@Target(ElementType.FIELD) 作用属性字段、枚举的常量
@Target(ElementType.METHOD) 作用方法
@Target(ElementType.PARAMETER) 作用方法参数
@Target(ElementType.CONSTRUCTOR) 作用构造函数
@Target(ElementType.LOCAL_VARIABLE)作用局部变量
@Target(ElementType.ANNOTATION_TYPE)作用于注解（@Retention注解中就使用该属性）
@Target(ElementType.PACKAGE) 作用于包
@Target(ElementType.TYPE_PARAMETER) 作用于类型泛型，即泛型方法、泛型类、泛型接口 （jdk1.8加入）
@Target(ElementType.TYPE_USE) 类型使用.可以用于标注任意类型除了 class （jdk1.8加入）
一般比较常用的是ElementType.TYPE类型


