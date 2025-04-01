

//function to create effect on screen
function createRain() {
    const rainContainer = document.querySelector('.weather-effects');
    rainContainer.innerHTML = '';
  
      for(let i = 0; i < 50; i++) {
          const drop = document.createElement('div');
          drop.className = 'rain-drop';
          drop.style.left = Math.random() * 100 + 'vw';
          drop.style.animationDelay = Math.random() * 2 + 's';
          rainContainer.appendChild(drop);
      }
  }
  
  function updateWeatherEffects(message) {
    if (message.toLowerCase().includes('rain')) {
        changeWeather('rain');
    } else if (message.toLowerCase().includes('cloud')) {
        changeWeather('cloudy');
    }
  }
  
  function initWeatherEffects(){
    const effectsContainer = document.createElement('div');
    effectsContainer.className = 'weather-effects';
    document.body.appendChild(effectsContainer);
  }
  
    function updateWeatherBackground(condition){
      const body =document.body;
  
      if(condition === 'rain'){
          body.classList.add('rainy-theme');
          createRain();
      } else if (condition === 'snow') {
          body.classList.add('snow-theme');
          createSnow();
      } else if (condition === 'sunny'){
          body.classList.add('sunny-theme');
          createSunny();
      }
  }
  
  
  //snow activation
  function showSnowEffect(){
      const weatherEffects = document.querySelector('.weather-effects');
      weatherEffects.innerHTML = `
          <div class="snow-flake"></div>
          <div class="snow-flake" style="left:15%"></div>
          <!-- Mais flocos... -->
      `;
  }
  
  function changeWeather(condition) {
    const effects = document.querySelector('.weather-effects');
    effects.innerHTML = ''; // Limpa efeitos anteriores
    
    if(condition === 'rain') {
      createRain();
      effects.innerHTML += '<div class="fog-layer"></div>';
    }
    else if(condition === 'cloudy') {
      effects.innerHTML = `
        <div class="cloud" style="top:30%">☁️</div>
        <div class="cloud" style="top:50%">⛅</div>
        <div class="fog-layer"></div>
      `;
    }
  }