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
