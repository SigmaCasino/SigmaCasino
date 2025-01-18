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
let bet;
const betNumers = [1,2,3,4];
betNumers.forEach((number) => {
    let button = document.getElementById(`button-${number}`);
    button.addEventListener("click", () => {
        bet = number; 
        betNumers.forEach((number) => {
            let button = document.getElementById(`button-${number}`);
            button.querySelector('.selected').classList.add("opacity-0");
        })
        button.querySelector('.selected').classList.remove("opacity-0");
    });
})

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

// Temporary function to generate times for each horse
const generateTimes = () => {
    const times = [];
    for (let i = 0; i < 4; i++) {
        const time = Math.floor(Math.random() * 15000) + 5000; // Random time between 1 and 5 seconds
        times.push(time);
    }
    return times;
}

// Makes horse movement not linear
generateCubicBezier = () => {
    let cubicBezier = [];
    for(i=0; i<4; i++){
        cubicBezier.push(Math.random());
    }
    return cubicBezier;
}

// Applies styles
setDurationProperties = (trackContainer, time) => {
    let trackEl = trackContainer.querySelector('.horse-track-indicator');
    let horseEl = trackContainer.querySelector('.horse');
    let cubicBezier = generateCubicBezier();
    let randomTransition = `right ${time / 1000}s cubic-bezier(${cubicBezier[0]}, ${cubicBezier[1]}, ${cubicBezier[2]}, ${cubicBezier[3]})`;
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


const resetHorses = () => {
    horseTracks.forEach((trackContainer, i) => {
        trackContainer.querySelector('.horse').style.transition = 'none';
        trackContainer.querySelector('.horse-track').style.transition = 'none';
        trackContainer.querySelector('.horse').style.right = '100%';
        trackContainer.querySelector('.horse-track').style.right = '100%';
    });
}

// Start button logic
startButton.addEventListener('click', () => {
    setTimeout(()=>{

        started=true;
        horsesTimes = generateTimes();
        // adjust result to be the actual result from the backend

        console.log(
            `Horse 1: ${horsesTimes[0]}ms\nHorse 2: ${horsesTimes[1]}ms\nHorse 3: ${horsesTimes[2]}ms\nHorse 4: ${horsesTimes[3]}ms`
        )

        const winner = horsesTimes.indexOf(Math.min(...horsesTimes)) + 1;

        console.log('Winner: ', winner);

        startButton.disabled = true;
        horseTracks.forEach((trackContainer, i) => {
            setDurationProperties(trackContainer, horsesTimes[i]);
        });
        document.querySelectorAll('.horse-track-indicator, .horse').forEach(el => {
            el.style.right = '0';
        });

        // this will be from th
        if(true){
            showWinningMessage(10000);
        }

        setTimeout(()=>{
            // resetHorses();
        }, 4000)


    }, 1000)


    return

    if (!bet || amountInput.value == 0) return;

    // Replace with the actual URL of your Spark backend
    const backendUrl = "https://your-spark-backend.com/api/place-bet";
    
    // Prepare the data to send
    const requestData = {
        bet: bet,
        amount: parseFloat(amountInput.value), // Ensure it's sent as a number
        userId: userId // Pass the userId (ensure this is available in your scope)
    };
    
    // Optional: Add headers if necessary for your backend
    const headers = {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${userToken}` // Replace with your actual user token if using authentication
    };
    



    // Send the request to the backend
    fetch(backendUrl, 
        {
            method: "POST", // Typically use POST for sending data
            headers: headers,
            body: JSON.stringify(requestData) // Convert the data to JSON
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to place bet. Please try again.");
            }
            return response.json(); // Parse JSON response
        })
        .then(data => {

            console.log("Bet placed successfully:", data);

            horsesTimes = generateTimes();
            // adjust result to be the actual result from the backend

            console.log(
                `Horse 1: ${horsesTimes[0]}ms\nHorse 2: ${horsesTimes[1]}ms\nHorse 3: ${horsesTimes[2]}ms\nHorse 4: ${horsesTimes[3]}ms`
            )
            console.log('Winner: ', horsesTimes.indexOf(Math.min(...horsesTimes)) + 1);

            startButton.disabled = true;
            horseTracks.forEach((trackContainer, i) => {
                setDurationProperties(trackContainer, horsesTimes[i]);
            });
            document.querySelectorAll('.horse-track, .horse').forEach(el => {
                el.style.right = '0';
            });


            // Handle the response, e.g., show a success message or update the UI
        })
        .catch(error => {
            console.error("Error placing bet:", error);
            // Show an error message to the user
        });

})