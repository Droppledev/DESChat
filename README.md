# DESChat
Implementation of DES in Chat on Android with two users, Alice and Bob

- Master Branch is the original, which the DESKey is pre-determined, so Alice and Bob know the DESKey 
- DES-KDC-Chat Branch is implementation of DES using KDC, with assumption Alice and Bob have contacted the KDC, then KDC will generate Session Key which is will be used for DESKey
- KDC Server Branch is the implementation of KDC Server that will generate session key
