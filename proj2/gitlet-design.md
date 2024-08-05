# Gitlet Design Document

**Name**: **Ruiyan Han**

## Classes and Data Structures

### Class: Commit

提交一个commit的流程：

- 实例化commit对象
- 将需要add的文件写入blobs，同时将该文件复制到Object文件夹中（文件名为其哈希值），将需要remove的文件从blobs中删除

- 将commit写入Object文件夹（使用writeCommit方法）
- 设置当前的Head以及分支Head指针（使用makeHead、makeBranchHead方法）
- 清空staging area

#### Instance variables:

- message: 储存该commit的提交信息
- timestamp：储存该commit的提交时间，在constructor内确定
- blobs：TreeMap<String, String> , 用于储存文件名与其SHA-1哈希值的对应（不使用HashMap的原因：其中储存的顺序的不确定的，treeMap是确定的）
- parent：储存该commit的parent的哈希值
- secondParent：储存该commit的第二parent的哈希值
- commitID： 储存该commit自身的哈希值，由timestamp，message，blobs，parent确定

#### Methods

- Commit(String message, TreeMap<String, String> blobs, String parent) : constructor
- createCommitID: 根据commitID的规则来创造该commit的ID，供constructor使用
- makeHead(String branchName)：使得当前的Head指针指向当前的commit，即在Heads文件夹中创建以branchName为名，commitID为文件内容的文件。
- makeBranchHead(String branchName)：使得分支的Head指针指向当前的commit，即在Branches文件夹中创建以branchName为名，commitID为文件内容的文件
- getHead：返回当前的Head Commit的ID
- getCurrentBranch：返回当前branch的名称
- writeCommit：序列化commit对象，将当前commit写入Object文件夹
- findCommit：根据给定的CommitID返回对应的Commit


### Class Repository

#### Instance variables：

- CWD：当前工作目录
- GITLET_DIR：.gitlet文件夹，储存所有相关内容
- OBJECT_FOLDER：储存commit以及追踪的文件
- HEADS_FOLDER：储存Head指针
- STAGING_FOLDER：储存被add或remove的文件
- STAGING_ADDITION_FOLDER: 储存被add的文件
- STAGING_REMOVAL_FOLDER: 储存被remove的文件
- BRANCH_FOLDER：储存分支HEAD

主要是整合其他class中的方法，供Main class使用。

结构：

.gitlet/Heads/branches/

​					  /head file

​		 /index/addition./

​					/removal/

​		 /OBJECT/Commits/

​						/object files

### Init

#### Methods：

- setGitlet() ：创建.gitlet文件夹，提交信息为"initial commit"的commit



### Log

#### Methods：

- log: 从当前commit开始，依次向前(沿着parent，忽略second parent)打印各个commit的信息（调用printCommitMSG）
- globalLog：打印所有存在的commit的信息，不计顺序（调用printCommitMSG）
- printMergeMSG：打印有Merge存在的commit的信息
- printCommitMSG：打印某一个commit的信息（根据是否存在second parent来判断是否需要调用printMergeMSG）



### Add

#### Methods:

- isStaged: 判断某一个文件是否被staged
- copyFile：复制文件到指定路径
- copyFileToStage：复制文件到staging area
- isSameAsCurrentCommit：判断给定文件的版本是否与当前commit一致
- removeAddFromStage：从staging area中移除需要add的文件
- removeRemovalFromStage：从staging area中移除需要remove的文件
- clearStagingArea：清除staging area，调用上面两个方法
- isStageEmpty：判断staging area是否为空



### Branch

#### Methods：

- createBranch：创建新branch
- isBranchExist：判断branch是否存在
- deleteBranch：删除branch



### Status

#### Methods：

- printBranch：打印存在的branch以及当前branch信息
- printStaged：打印被stage for addition的文件
- printRemoval：打印被stage for removal的文件



### Checkout

### Methods

- checkoutFile：将当前最新版本的文件写入工作目录，使用checkoutFileInCommit(headID, filename)
- findFullCommitID：根据commitID的缩写找到完整的commitID，供下面的方法调用
- checkoutFileInCommit：将指定版本的文件写入工作目录
- checkoutBranch：checkout该branch中所有的文件，并且更换当前branch，调用了checkoutByCommitID
- checkoutByCommitID：checkout某一commit中的所有文件
- isFileTracked：判断某一文件是否被给定的blobs跟踪
- isToOverwrite：判断某一文件是否将被给定的blobs覆写



### Merge

#### Methods

- getBranchHead：得到某一分支的head
- splitPoint：找到最近的共同祖先，调用ancestors
- firstIsAncestorOfSecond：判断第一个ID是否为第二个ID的祖先
- ancestors：使用TreeSet返回某一commit的祖先，使用递归访问每一个commit
- merge：将当前分支与给定的分支合并，分多种情况讨论，调用其他的help method
- checkUntrackedFile: 判断CWD中的文件是否会被删除或覆写，供merge调用
- isToOverwrite：判断文件是否会被覆写
- dealWithConflict：处理合并时出现冲突的情况，供merge调用
- readContentsAsString：以String的形式返回文件的内容。
- isFileSameInCommits：判断两个不同commit中的文件版本是否一致
- existsIn：判断给定文件是否存在于某个commit
- isModifiedIn：判断文件在两个commit中是否发生变化，直接调用isFileSameInCommits
- allFileNames：返回split point，当前commit，给定分支的head这三者所跟踪的所有文件的文件名构成的list，调用FileNames
- FileNames：返回某个commit中追踪的所有文件



### Main

根据输入的命令调用Repository中的方法。


## Algorithms

## Persistence

