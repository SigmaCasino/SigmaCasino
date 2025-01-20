// ROULETTE FIELDS
const rouletteItems = [
    {number: 0, color: "green"},
    {number: 32, color: "red"},
    {number: 15, color: "black"},
    {number: 19, color: "red"},
    {number: 4, color: "black"},
    {number: 21, color: "red"},
    {number: 2, color: "black"},
    {number: 25, color: "red"},
    {number: 17, color: "black"},
    {number: 34, color: "red"},
    {number: 6, color: "black"},
    {number: 27, color: "red"},
    {number: 13, color: "black"},
    {number: 36, color: "red"},
    {number: 11, color: "black"},
    {number: 30, color: "red"},
    {number: 8, color: "black"},
    {number: 23, color: "red"},
    {number: 10, color: "black"},
    {number: 5, color: "red"},
    {number: 24, color: "black"},
    {number: 16, color: "red"},
    {number: 33, color: "black"},
    {number: 1, color: "red"},
    {number: 20, color: "black"},
    {number: 14, color: "red"},
    {number: 31, color: "black"},
    {number: 9, color: "red"},
    {number: 22, color: "black"},
    {number: 18, color: "red"},
    {number: 29, color: "black"},
    {number: 7, color: "red"},
    {number: 28, color: "black"},
    {number: 12, color: "red"},
    {number: 35, color: "black"},
    {number: 3, color: "red"},
    {number: 26, color: "black"}
];



// Spin logic
const spinButton = document.getElementById("spinButton");
const roulette = document.getElementById("roulette");
const amountInput = document.getElementById("amount-input");
const clearAmount = document.getElementById("clear-amount");
let currentRotation = 0;
let isSpinning = false;

// Gets angle for given number
const getAngle = (number) => {
    console.log(number);
    console.log(typeof number);
    let index = rouletteItems.findIndex(item => item.number === number);
    return 360 - (index * 360/37);
}

// Disables button
const disableButton = () => {
    isSpinning = true;
    spinButton.disabled = true;
    spinButton.classList.remove("spin-button-enabled");
    spinButton.classList.add("spin-button-disabled");
    spinButton.innerHTML = "Spinning...";
}

// Enables button
const enableButton = () => {
    isSpinning = false;
    spinButton.disabled = false;
    spinButton.classList.remove("spin-button-disabled");
    spinButton.classList.add("spin-button-enabled");
    spinButton.innerHTML = "Spin";
}   

// AMOUNT
const amountButtonsValues = [10, 50, 100, 500];
clearAmount.addEventListener("click", () => {
    amountInput.value = 0;
})
amountButtonsValues.forEach((value) => {
    const button = document.getElementById(`amount-button-${value}`);
    button.addEventListener("click", () => {
        console.log(`Selected amount: ${value}`);
        amountInput.value = Number(amountInput.value) + value;
    });

})

// WINNING MESSAGE
const showWinningMessage = (amount) => {

    const message = document.createElement('div');
    
    message.classList.add(
        'opacity-0', 
        'fixed', 
        'duration-500', 
        'size-[500px]', 
        'z-[100]', 
        'text-8xl', 
        'flex', 
        'flex-col', 
        'items-center', 
        'justify-center', 
        'font-bold', 
        'top-1/3', 
        'left-1/2', 
        '-translate-y-1/2', 
        '-translate-x-1/2'
    );
    message.style.background = 'radial-gradient(circle, black, transparent 70%)';
    message.innerHTML = `
        <span class='text-white font-semibold'>You've won</span> 
        <span class='text-action'>${amount}$</span>`;
    
    document.body.appendChild(message);

    setTimeout(() => {
        message.classList.remove('opacity-0'); 
    }, 10); 

    // Hide the message after 2 seconds
    setTimeout(() => {
        message.classList.add('opacity-0'); // Fade out
        setTimeout(() => {
            message.remove(); // Remove the element from the DOM after the fade-out
        }, 500); // Match the duration of the fade-out animation
    }, 3000);

}


// BET
const betInput = document.getElementById("color");
const betColors = ['red', 'black', 'green'];
betColors.forEach((color) => {
    let button = document.getElementById(`${color}-button`);
    button.addEventListener("click", () => {
        removeSelected();
        selectColor(color);
        
    });
})
const selectColor = (color) => {
    let button = document.getElementById(`${color}-button`);
    button.querySelector('.selected').classList.remove("opacity-0");
    betInput.value = color;
}
const removeSelected = () => {
    betColors.forEach((color) => {
        let button = document.getElementById(`${color}-button`);
        button.querySelector('.selected').classList.add("opacity-0");
    })
}


// SPIN
// INITIALIZE VALUES
date_ = date && date.trim();
betAmount_ = betAmount && betAmount.trim();
guess_ = guess && guess.trim();
result_ = result && result.trim();

// SET VALUES TO PREVIOUSLY SELECTED
if(guess_){
    selectColor(guess_)
}
if(betAmount_){
    amountInput.value = Number(betAmount_);
}

if(date_ && betAmount_ && guess_ && result_){
    // backend call for number
    disableButton();
    setTimeout(()=>{
    // backend call for number (rn is random)
        console.log(`Winning number generated by backend: ${result_}`);
        
        // 3-10 rotations
        let additionalRotations = Math.floor(Math.random() * 8) + 3;
        let angle = getAngle(Number(result_));
        console.log(angle);

        // Adjusting final angle
        let finalAngle = Math.ceil(currentRotation/360) * 360 + angle + 360 * additionalRotations
        currentRotation = finalAngle;

        // Applying style
        roulette.style.transform = `rotate(${finalAngle}deg)`;

        setTimeout(_=>{
            if(rouletteItems.find(el => el.number == result_).color === guess_){
                let won = 0;
                if(result === 'green'){
                    won = betAmount_ * 30;
                } else won = betAmount_ * 2;
                showWinningMessage(won);
            }
            removeSelected();
            amountInput.value = 0;
            enableButton();
        }, 5000)
        
    }, 100)
}


