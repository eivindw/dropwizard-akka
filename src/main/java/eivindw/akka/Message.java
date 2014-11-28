package eivindw.akka;

public class Message<T> {

   private final String corId;
   private final T messageData;

   public Message(String corId, T messageData) {
      this.corId = corId;
      this.messageData = messageData;
   }

   public String corId() {
      return corId;
   }

   public T messageData() {
      return messageData;
   }
}
