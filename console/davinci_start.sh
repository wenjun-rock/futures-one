prev=`pwd`
base=$prev
lib1=`cd $base/../futures-davinci/target/ && pwd`
lib2=`cd $base/../futures-davinci/target/lib && pwd`
cd $prev
echo $lib1
echo $lib2
cp1=`ls $lib1/*.jar | xargs | sed "s/ /:/g"`
cp2=`ls $lib2/*.jar | xargs | sed "s/ /:/g"`
cp=$base/conf:$cp1:$cp2
echo $cp

nohup java -cp $cp fwj.futures.resource.RestLaunch >> log/davinci.log 2>&1 &

