package issue

import org.junit._
import java.io.{ ObjectOutputStream, ObjectInputStream, ByteArrayOutputStream, ByteArrayInputStream }
import scala.reflect.ClassTag

class IssueTest {
  @Test
  def testSerialization: Unit = {
    val obj = List(new Meh)
    val arr = serialize(obj)
    val obj2 = deserialize[List[Meh]](arr)
    assert(obj == obj2)
  }

  @Test
  def testMyCollection: Unit = {
    val list = List(1, 2, 3)
    val arr = serialize(new MyCollection(list))
    val obj2 = deserialize[MyCollection[Int]](arr)
    assert(obj2.list == list)
  }

  def serialize[A <: Serializable](obj: A): Array[Byte] = {
    val o = new ByteArrayOutputStream()
    val os = new ObjectOutputStream(o)
    os.writeObject(obj)
    o.toByteArray()
  }

  def deserialize[A <: Serializable: ClassTag](bytes: Array[Byte]): A = {
    val s = new ByteArrayInputStream(bytes)
    val is = ScalaObjectInputStream[A](s)
    is.readObject
  }
}

class MyCollection[B](val list: List[B]) extends scala.collection.Iterable[B] {
  override def iterator = list.iterator
  // protected[this] override def writeReplace(): AnyRef = this
}
