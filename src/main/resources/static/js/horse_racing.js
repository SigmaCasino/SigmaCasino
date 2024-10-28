// DOM elements
const gameContainer = document.getElementById('gameContainer');
const startButton = document.getElementById('startButton');

// horse racing variables
let horsesTimes;
const horseColors = ['#ef4444', '#22d3ee', '#16a34a', '#9333ea'];
const horseTracks = [];

// Create horse tracks
for (let i = 0; i < 4; i++) {
    const horseTrack = document.createElement('div');
    horseTrack.classList.add('horse-track-container');
    horseTrack.id = `horse${i + 1}`; // Assign unique ID to each horse track
    horseTracks.push(horseTrack); // Add horse track to array for later reference

    // Create horse track elements
    const track = document.createElement('div');
    track.classList.add('horse-track');
    track.innerHTML = `<div class="horse-track-indicator" style="background-color: ${horseColors[i]}"></div>`;

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
        const time = Math.floor(Math.random() * 5000) + 5000; // Random time between 1 and 5 seconds
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
    let trackEl = trackContainer.querySelector('.horse-track');
    let horseEl = trackContainer.querySelector('.horse');
    let cubicBezier = generateCubicBezier();
    let randomTransition = `right ${time / 1000}s cubic-bezier(${cubicBezier[0]}, ${cubicBezier[1]}, ${cubicBezier[2]}, ${cubicBezier[3]})`;
    horseEl.style.transition = randomTransition;
    trackEl.style.transition = randomTransition;
}

// Start button logic
startButton.addEventListener('click', () => {
    setTimeout(()=>{

        horsesTimes = generateTimes();

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
        
    }, 100)


})



