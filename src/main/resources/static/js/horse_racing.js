// DOM elements
const gameContainer = document.getElementById('gameContainer');
const startButton = document.getElementById('startButton');
const amountInput = document.getElementById("amount-input");
const clearAmount = document.getElementById("clear-amount");


// horse racing variables
let horsesTimes;
const horseColors = ['#ef4444', '#22d3ee', '#16a34a', '#9333ea'];
const horseTracks = [];


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


// BET
const betNumers = [1,2,3,4];
betNumers.forEach((number) => {
    let button = document.getElementById(`button-${number}`);
    button.addEventListener("click", () => {
        removeSelected();
        selectHorse(number);
    });
})
const removeSelected = () => {
    betNumers.forEach((number) => {
        let button = document.getElementById(`button-${number}`);
        button.querySelector('.selected').classList.add("opacity-0");
    })
}
const selectHorse = (number) => {
    let button = document.getElementById(`button-${number}`);
    button.querySelector('.selected').classList.remove("opacity-0");
}

// Create horse tracks
for (let i = 0; i < 4; i++) {
    const horseTrack = document.createElement('div');
    horseTrack.classList.add('horse-track-container');
    horseTrack.id = `horse${i + 1}`; // Assign unique ID to each horse track
    horseTracks.push(horseTrack); // Add horse track to array for later reference

    // Create horse track elements
    const track = document.createElement('div');
    track.classList.add('horse-track');

    const trackIndicator = document.createElement('div');  
    trackIndicator.classList.add('horse-track-indicator');
    trackIndicator.style.backgroundColor = horseColors[i];
    track.appendChild(trackIndicator);

    const horse = document.createElement('div');
    horse.classList.add('horse');
    horse.style.borderColor = horseColors[i];
    horse.innerHTML = '🐎';

    horseTrack.appendChild(track);
    horseTrack.appendChild(horse);

    gameContainer.appendChild(horseTrack);
}


// Applies styles
setDurationProperties = (trackContainer, time, bezierCurves) => {
    let trackEl = trackContainer.querySelector('.horse-track-indicator');
    let horseEl = trackContainer.querySelector('.horse');
    let randomTransition = `right ${time / 1000}s cubic-bezier(${bezierCurves[0]}, ${bezierCurves[1]}, ${bezierCurves[2]}, ${bezierCurves[3]})`;
    horseEl.style.transition = randomTransition;
    trackEl.style.transition = randomTransition;
    console.log(trackEl);
}


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
    
    gameContainer.appendChild(message);

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

// Start button logic
date_ = date && date.trim();
betAmount_ = betAmount && betAmount.trim();
guess_ = guess && guess.trim();
try{
    if(times) times_ = JSON.parse(times);
    else times_ = null;
}
catch(e){
    console.error('times is not a valid JSON', times);
}
try{
    if(bezierCurves) bezierCurves_ = JSON.parse(bezierCurves);
    else bezierCurves_ = null;
}
catch(e){
    console.error('bezierCurves is not a valid JSON', bezierCurves);
}

console.log(date_);
console.log(betAmount_);
console.log(guess_);
console.log(times_);
console.log(bezierCurves_);


console.log(bezierCurves_);

if(guess_){
    selectHorse(guess_)
}
if(betAmount_){
    amountInput.value = Number(betAmount_);
}


const disableButton = () => {
    startButton.disabled = true;
    startButton.classList.add('opacity-50');
    startButton.innerHTML = 'Race started...';
}
const enableButton = () => {
    startButton.disabled = false;
    startButton.classList.remove('opacity-50');
    startButton.innerHTML = 'START RACE'
}


if(date_ && betAmount_ && guess_ && times_ && bezierCurves_ && bezierCurves_.length && times_.length){
    setTimeout(()=>{
    
        disableButton();
        started=true;
        horsesTimes = times_;
        // adjust result to be the actual result from the backend
    
        console.log(
            `Horse 1: ${horsesTimes[0]}ms\nHorse 2: ${horsesTimes[1]}ms\nHorse 3: ${horsesTimes[2]}ms\nHorse 4: ${horsesTimes[3]}ms`
        )
    
        const winner = horsesTimes.indexOf(Math.min(...horsesTimes)) + 1;
    
        console.log('Winner: ', winner);
    
        startButton.disabled = true;
        horseTracks.forEach((trackContainer, i) => {
            setDurationProperties(trackContainer, horsesTimes[i], bezierCurves_.slice(i*4, i*4+4));
        });
        document.querySelectorAll('.horse-track-indicator, .horse').forEach(el => {
            el.style.right = '0';
        });
    
        // this will be from th
        setTimeout(_=>{
            if(guess_ == winner){
                showWinningMessage(betAmount_*2);
            }
            removeSelected();
            amountInput.value = 0;
            enableButton();
        }, Math.max(...horsesTimes));

    }, 100)
}
    

