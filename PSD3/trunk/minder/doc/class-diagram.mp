input metauml;

iInterface.foreColor := white;
iClass.foreColor := white;

iInterface.iName.iFont.name := "cmtex10";
iClass.iName.iFont.name := "cmtex10";
iInterface.iMethodStack.iPict.iFont.name := "cmtex10";
iClass.iAttributeStack.iPict.iFont.name := "cmtex10";

beginfig(1);

  Interface.Recipient("Recipient")(
    "+getUid():String",
    "+getEmailAddresses():Set<EmailAddress>"
    );

  Interface.User("User")(
    "+authenticate(password:String):boolean",
    "+getDisplayName():String",
    "+getPrivilege():Privilege"
    );
  
  Class.Privilege("Privilege")("RECIPIENT","EVENTMANAGER","ADMINISTRATOR")();
  classStereotypes.Privilege("«enumeration»");
  
  Interface.RecipientStore("RecipientStore")(
    "+authenticateUser(username:String,password:String):User",
    "+addUser(displayName:String,username:String,password:String,emailAddress:InternetAddress,privilege:Privilege):void",
    "+createMailingList(label:String,ownerUsername:String):void",
    "+addUserToMailingList(label:String,username:String):void",
    "+searchRecipients(pattern:String):Set<Recipient>",
    );
  
  leftToRight(30)(User,Privilege);
  
  topToBottom(20)(Recipient,User);
  
  topToBottom(20)(RecipientStore,Recipient);
  
  drawObjects(
    Recipient,User,
    Privilege,RecipientStore
    );

  clink(inheritance)(User,Recipient);
  
endfig;

end