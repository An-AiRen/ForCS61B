<<<<<<< HEAD
# pro1a

### 未达成 

- [ ] Style
- [ ] d008:Fill up, empty, fill up again
- [ ] d009:Creating multiple ADs
- [ ] e002:AD-memory: Ensure that you resize down after removals

第一个错误不想理它了，后面几个还没弄明白原因。



### 总结

**理解abstraction**：在编程领域，将一个功能抽象化处理是非常重要的。比如在构建ArrayDeque时，当减法或加法遇到`index == 0`或者`index == items.length`的情况时，就不能按照加法或减法原来的定义对index进行操作，因此需要分类讨论。加减法遭遇数组边界是非常常见的，而在每个函数中都分条件判定略显麻烦和冗余，所以我们可以将 *+* 和 *-* 抽象为函数 *minusOne(int index)* 和 *plusOne(int index)*。

```java
private int minusOne(int index) {
    if (index == 0) {
        index = items.length - 1;
    } else {
        index -= 1;
    }
    return index;
}
```

原先使用-的地方，就用minusOne替代，可以省很多事儿。比如原先printDeque()函数的实现，若end <  start，就需要两个循环来实现此功能，一个打印start右边，一个打印end左边。现在有了minusOne和plusOne，便可以向对待顺序的数组那样对待AD了。

```java
for (int i = start; i != end; i = plusOne(i)) {
        System.out.print(items[i] + " ");
}
```



=======
# pro1a

### 未达成 

- [ ] Style
- [ ] d008:Fill up, empty, fill up again
- [ ] d009:Creating multiple ADs
- [ ] e002:AD-memory: Ensure that you resize down after removals

第一个错误不想理它了，后面几个还没弄明白原因。



### 总结

**理解abstraction**：在编程领域，将一个功能抽象化处理是非常重要的。比如在构建ArrayDeque时，当减法或加法遇到`index == 0`或者`index == items.length`的情况时，就不能按照加法或减法原来的定义对index进行操作，因此需要分类讨论。加减法遭遇数组边界是非常常见的，而在每个函数中都分条件判定略显麻烦和冗余，所以我们可以将 *+* 和 *-* 抽象为函数 *minusOne(int index)* 和 *plusOne(int index)*。

```java
private int minusOne(int index) {
    if (index == 0) {
        index = items.length - 1;
    } else {
        index -= 1;
    }
    return index;
}
```

原先使用-的地方，就用minusOne替代，可以省很多事儿。比如原先printDeque()函数的实现，若end <  start，就需要两个循环来实现此功能，一个打印start右边，一个打印end左边。现在有了minusOne和plusOne，便可以向对待顺序的数组那样对待AD了。

```java
for (int i = start; i != end; i = plusOne(i)) {
        System.out.print(items[i] + " ");
}
```



>>>>>>> eb12c4539ae58d7d2195e111503377debdb76b2c
**resize的重要性**：虽然我resize的部分基本没过:sob:，但是Josh还是让我懂得了resize的重要性。