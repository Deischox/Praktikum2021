let x,y;
let xspeed, yspeed;
let r,g,b;
let bird;

function preload(){
    bird = loadImage("../images/twitter.jpg")
}

function setup() {
    createCanvas(800, 400);
    x = random(800);
    y = random(400);
    xspeed = 2;
    yspeed = 2;
    recolor();
    colorMode(HSB);
  }

  function recolor(){
    
    r = random(0,360);
    g = 100
    b = 100
    
  }
  
  function draw() {
    background(0);
    //rect(x, y, 40, 30);
    image(bird,x,y,60,60);
    
    tint(r,g,b);
    x += xspeed;
    y += yspeed;
    
    if(x + 60 >= width)
    {
      x = width -60;
      xspeed = -xspeed;
      recolor();
    }else if(x <= 0)
    {
      x = 0;
      xspeed = -xspeed;
      recolor();
    }
    
    if(y + 60 >= height)
    {
      y = height -60;
      yspeed = -yspeed;
      recolor();
    }else if(y <= 0)
    {
      yspeed = -yspeed;
      y = 0;
      recolor();
    }

    fill(255);
    textSize(60);
    textFont("Georgia");
    text("Hashtag Analyse", (width-textWidth())/2, height/2);
}