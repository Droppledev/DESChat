import * as functions from 'firebase-functions';

// Start writing Firebase Functions
// https://firebase.google.com/docs/functions/typescript

let d = new Date()
let keyAlice = "alicecan"
let keyBob = "bobgants"
let initSesKey = "doraemon"

let initDay = 29

function getSessionKey(){
    let concatKey = keyAlice+keyBob
    let curDate = d.getDate()
    if(curDate!=initDay){
        let emptyString=""
        while (emptyString.length < 8) {
            emptyString += concatKey[Math.floor(Math.random() * concatKey.length)];
        }
        initSesKey = emptyString
        initDay = curDate
    }
    return initSesKey
}

export const session = functions.https.onRequest((request, response) => {
    
    response.json({"seskey":`${getSessionKey()}`})
});
