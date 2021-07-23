### proj1b

#### 总结

总体难度跟proj1a不是一个量级的，教程事无巨细，堪称保姆级教程。Josh老哥真的手把手教你如何边实现功能边用Junit写测试，算是体验了一把TDD。

一个非常重要的收获跟做完proj1a后的体验类似，就是抽象化的重要性。OffByOne的equal函数并不是判断两者是否相等而是在字母表中是否相差1，这样看来我们想让equal是什么它就是什么，不需要拘泥于这个词或数学符号在创立之初原本的意思。、



**Error** interface不能放在new的右边，像这样：``CharacterComparator c = new CharacterComparator;``