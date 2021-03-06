8b64c2f833825f8a8d50d4130af44c ����   2 �  urbanairship/App  java/lang/Object  java/io/Serializable name Ljava/lang/String; key secret masterSecret pushServiceStatus 
pushStatus storekitMode appUrl androidPackageName blackberryUsername blackberryPassword blackberryPushUrl richpushEnabled Z certificate certificatePassword <init> ()V Code
    	     LineNumberTable LocalVariableTable this Lurbanairship/App; isProduction ()Z	  &   ( 
production
 * , + java/lang/String - . equalsIgnoreCase (Ljava/lang/String;)Z StackMapTable isDevelopment 2 development 
isDisabled 5 disabled getName ()Ljava/lang/String;	  9   setName (Ljava/lang/String;)V getKey	  > 	  setKey 	getSecret	  B 
  	setSecret getMasterSecret	  F   setMasterSecret getPushServiceStatus setPushServiceStatus getPushStatus	  L   setPushStatus getStorekitMode	  P   setStorekitMode 	getAppUrl	  T   	setAppUrl getAndroidPackageName	  X   setAndroidPackageName getBlackberryUsername	  \   setBlackberryUsername getBlackberryPassword	  `   setBlackberryPassword getBlackberryPushUrl	  d   setBlackberryPushUrl isRichpushEnabled setRichpushEnabled (Z)V getCertificate	  k   setCertificate getCertificatePassword	  o   setCertificatePassword toString s java/lang/StringBuilder
  u 6 7
 * w x y valueOf &(Ljava/lang/Object;)Ljava/lang/String;
 r {  ; } : 
 r  � � append -(Ljava/lang/String;)Ljava/lang/StringBuilder;
  � H 7
 r � q 7 
SourceFile App.java !            	     
                                                                #        <     
*� *� �              	          
 ! "    # $     J     *� %� '*� %� )� ��                     ! "   /      0 $     J     *� %� 1*� %� )� ��                     ! "   /      3 $     J     *� %� 4*� %� )� ��           #          ! "   /      6 7     /     *� 8�           (          ! "    : ;     >     *+� 8�       
    -  .          ! "          < 7     /     *� =�           2          ! "    ? ;     >     *+� =�       
    7  8          ! "      	    @ 7     /     *� A�           <          ! "    C ;     >     *+� A�       
    A  B          ! "      
    D 7     /     *� E�           F          ! "    G ;     >     *+� E�       
    K  L          ! "          H 7     /     *� %�           P          ! "    I ;     >     *+� %�       
    U  V          ! "          J 7     /     *� K�           Z          ! "    M ;     >     *+� K�       
    _  `          ! "          N 7     /     *� O�           d          ! "    Q ;     >     *+� O�       
    i  j          ! "          R 7     /     *� S�           n          ! "    U ;     >     *+� S�       
    s  t          ! "          V 7     /     *� W�           x          ! "    Y ;     >     *+� W�       
    }  ~          ! "          Z 7     /     *� [�           �          ! "    ] ;     >     *+� [�       
    �  �          ! "          ^ 7     /     *� _�           �          ! "    a ;     >     *+� _�       
    �  �          ! "          b 7     /     *� c�           �          ! "    e ;     >     *+� c�       
    �  �          ! "          f $     /     *� �           �          ! "    g h     >     *� �       
    �  �          ! "          i 7     /     *� j�           �          ! "    l ;     >     *+� j�       
    �  �          ! "          m 7     /     *� n�           �          ! "    p ;     >     *+� n�       
    �  �          ! "          q 7     H     � rY*� t� v� z|� ~*� �� ~� ��           �          ! "    �    �