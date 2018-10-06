package issue

import java.io.{ ObjectInputStream, InputStream, ObjectStreamClass }
import scala.reflect.ClassTag

class ScalaObjectInputStream[A: ClassTag] private (in: InputStream) extends InputStream {
  private[this] val cl: ClassLoader =
    implicitly[ClassTag[A]].runtimeClass.getClassLoader
  private[this] val underlying = new ScalaObjectInputStream.ScalaObjectInputStreamImpl(in)
  override def available: Int = underlying.available
  override def close(): Unit = underlying.close()
  override def mark(readlimit: Int): Unit = underlying.mark(readlimit)
  override def markSupported: Boolean = underlying.markSupported
  override def read: Int = underlying.read
  override def read(b: Array[Byte]): Int = underlying.read(b)
  override def read(b: Array[Byte], off: Int, len: Int): Int = underlying.read(b, off, len)
  override def reset(): Unit = underlying.reset()
  override def skip(n: Long): Long = underlying.skip(n)
  def readObject: A = underlying.readObject.asInstanceOf[A]
}

object ScalaObjectInputStream {
  def apply[A: ClassTag](in: InputStream): ScalaObjectInputStream[A] =
    new ScalaObjectInputStream(in)

  private class ScalaObjectInputStreamImpl[A: ClassTag](in: InputStream)
      extends ObjectInputStream(in) {
    private[this] val cl: ClassLoader =
      implicitly[ClassTag[A]].runtimeClass.getClassLoader

    override protected def resolveClass(desc: ObjectStreamClass): Class[_] =
      try {
        cl.loadClass(desc.getName)
      } catch {
        case _: ClassNotFoundException => super.resolveClass(desc)
      }
  }
}
