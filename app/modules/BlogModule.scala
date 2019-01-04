package modules

import com.google.inject.AbstractModule
import services.BlogService

class BlogModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[BlogService]).asEagerSingleton()
  }
}
