const message = document.getElementById('message');
const messageContainer = document.getElementById('message-container');

if(message.innerHTML && message.innerHTML.trim().length > 0){
    console.log(message.innerHTML.length)
    console.log('message.innerHTML', message.innerHTML)
    console.log(Boolean(message.innerHTML))
    messageContainer.classList.remove('-translate-y-40');
    setTimeout(() => {
        messageContainer.classList.add('-translate-y-40');

        setTimeout(
            () => message.innerHTML = '',
            500
        )

    }, 5000);
  
}