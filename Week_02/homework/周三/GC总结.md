# GC总结
## 测试数据
|  GC类型   |  堆内存大小   | 创建对象数  | 总GC次数 | MinorGC次数 | MajorGC次数 | 总暂停时间(程序运行5s) | Avg creation rate | Avg promotion rate |
|  ----  | ----  |  ----  | ----  |  ----  | ----  |  ----  | ----  | ----  |
|  Serial  | 512m  |  38301  | 84  |  14  | 70  |  3 sec 450 ms  | 1.94 gb/sec  | 83.25 mb/sec  |
|  Serial  | 1g  |  67519  | 77  |  60  | 17  |  2 sec 100 ms  | 3.42 gb/sec  | 735.18 mb/sec  |
|  Serial  | 2g  |  63950  | 74  |  58  | 16  |  2 sec 240 ms  | 3.28 gb/sec  | 795.85 mb/sec  |
|  Serial  | 4g  |  64620  | 75  |  59  | 16  |  2 sec 250 ms  | 3.29 gb/sec  | 798 mb/sec  |
|  Parallel  | 512m  |  18215  | 122  |  24  | 98  |  4 sec 140 ms  | 949.27 mb/sec  | 95.55 mb/sec  |
|  Parallel  | 1g  |  70282  | 122  |  104  | 18  |  1 sec 710 ms  | 3.58 gb/sec  | 834.22 mb/sec  |
|  Parallel  | 2g  |  77506  | 71  |  58  | 13  |  1 sec 520 ms  | 3.93 gb/sec  | 665.52 mb/sec  |
|  Parallel  | 4g  |  86541  | 45  |  35  | 10  |  1 sec 310 ms  | 4.38 gb/sec  | 413.19 mb/sec  |
|  Parallel  | 8g  |  93523  | 29  |  23  | 6  |  900 ms  | 4.71 gb/sec  | 214.78 mb/sec  |
|  CMS  | 512m  |  34761  | 74  |  71  | 3  |  3 sec 390 ms  | 1.65 gb/sec  | 100.13 mb/sec  |
|  CMS  | 1g  |  69429  | 90  |  85  | 5  |  1 sec 700 ms  | 2.54 gb/sec  | 943.49 mb/sec  |
|  CMS  | 2g  |  70535  | 82  |  82  | 0  |  1 sec 410 ms  | 2.61 gb/sec  | 759.63 mb/sec  |
|  CMS  | 4g  |  60987  | 240  |  240  | 0  |  2 sec 410 ms  | 3.36 gb/sec  | 1.03 gb/sec  |
|  G1  | 512m  |  28091  | -  |  -  | -  |  2 sec 340 ms  | 1.52 gb/sec  | 382.96 mb/sec  |
|  G1  | 1g  |  81744  | -  |  -  | -  |  1 sec 620 ms  | 4.41 gb/sec  | 206.37 mb/sec  |
|  G1  | 2g  |  95060  | -  |  -  | -  |  1 sec  | 5.12 gb/sec  | 44.3 mb/sec  |
|  G1  | 4g  |  93890  | -  |  -  | -  |  660 ms  | 5.07 gb/sec  | 245.22 mb/sec  |
## Serial GC
串行 GC 是单线程的 GC 算法,不能充分利用 CPU 的多核资源,在垃圾回收时只能使用单个核心.使用串行 GC 算法CPU利用率高,暂停时间长,相较于其他 GC 算法,在堆内存为几百兆且为单核心 CPU 时性能较好.
## Parallel
并行 GC 适用于多核服务器,可以充分利用 CPU 的多核心资源,吞吐率较好.在 GC 期间,所有 CPU 内核都在并行清理垃圾,所以总暂停时间更短.在两次 GC 周期的间隔期,没有 GC 线程在运行,不会消耗任何系统资源.堆内存较小时,效率低,性能退化.
## CMS GC
CMS GC 其对年轻代采用并行 STW 方式的 mark-copy (标记-复制)算法,对老年代主要使用并发 mark-sweep (标记-清除)算法.CMS GC 的设计目标是避免在老年代垃圾收集时出现长时间的卡顿,主要通过两种手段来达成此目标:
1. 不对老年代进行整理,而是使用空闲列表（free-lists）来管理内存空间的回收.
2. 在 mark-and-sweep （标记-清除） 阶段的大部分工作和应用线程一起并发执行.
也就是说,在这些阶段并没有明显的应用线程暂停.但值得注意的是,它仍然和应用线程争抢CPU 时间.默认情况下,CMS 使用的并发线程数等于 CPU 核心数的 1/4.如果服务器是多核 CPU,并且主要调优目标是降低 GC 停顿导致的系统延迟,那么使用 CMS是个很明智的选择.进行老年代的并发回收时,可能会伴随着多次年轻代的 minor GC.
## G1 GC
在 G1 GC 中,堆不再分成年轻代和老年代,而是划分为多个（通常是 2048 个）可以存放对象的 小块堆区域(smaller heap regions).每个小块,可能一会被定义成 Eden 区,一会被指定为 Survivor 区或者 Old 区.在逻辑上,所有的 Eden 区和 Survivor区合起来就是年轻代,所有的 Old 区拼在一起就是老年代.这样划分之后,使得 G1 不必每次都去收集整个堆空间,而是以增量的方式来进行处理:每次只处理一部分内存块,称为此次 GC 的回收集(collection set).每次 GC 暂停都会收集所有年轻代的内存块,但一般只包含部分老年代的内存块.观察数据,当堆内存大小合适时 G1 GC 的吞吐率和暂停时间都较好.