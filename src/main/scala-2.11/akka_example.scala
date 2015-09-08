import akka.actor.ActorContext
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.PoisonPill
import akka.actor.Terminated

class ChildActor extends Actor{
	def receive = {
		case "stop" =>

			self ! PoisonPill
	}
}

class AnotherChildActor extends Actor{
	def receive = {
		case "stop" =>

			self ! PoisonPill
	}
}
object HelloSupervision {

	private var hellocon:ActorContext=null

	def exportContext(conte:ActorContext) =
	{
		hellocon=conte;
		println(hellocon.self.path)
	}
	//println(hellocon.self.path)
	def createChild(name:String):ActorRef =
		{

					val child=hellocon.actorOf(Props[ChildActor],name)
					hellocon watch child
					child
		}

	def createAnotherChild():ActorRef =
		{

			val child = hellocon.actorOf(Props[AnotherChildActor],"AnotherChildActor")
			hellocon watch child
			child
		}

}
object CreateSupervisor
{
	import TestSupervision._
	var count =0
	def createHelloActor =
	{
		count +=1
		println(count)
		val supervisor = system.actorOf(Props[HelloSupervision], s"HelloSupervision_${count}")
	}
}

class createHello
{

	CreateSupervisor.createHelloActor

	/*def createActor(name:String) =
	{
		helloSupervision.createChild(name) ! "stop"
		Thread.sleep(10000)
		helloSupervision.createAnotherChild() ! "stop"
	}
	Thread.sleep(10000)
	createActor("childActor")*/
}
class HelloSupervision extends Actor{

	import HelloSupervision._

	//val child = context.actorOf(Props[ChildActor], "ChildActor")
	//val anotherChild = context.actorOf(Props[ChildActor], "AnotherChildActor")
  hellocon=context
	//exportContext(context)

	//context.watch(child)
	//context.watch(anotherChild)

	def receive = {
		case "send" =>
			//child ! "stop"
			//anotherChild ! "stop"

		case Terminated(x) => println(s"child actor $x dead")
	}

}

object TestSupervision{

	val system = ActorSystem("MySystem")

	def main(args: Array[String]): Unit = {

		for(i<-1 to 10)
		{
			var hello=new createHello
			//Thread.sleep(1000)
			//hello.createActor("childActor")
		}

		//val supervisor = system.actorOf(Props[HelloSupervision], "HelloSupervision")

		//supervisor ! "send"
	}

}