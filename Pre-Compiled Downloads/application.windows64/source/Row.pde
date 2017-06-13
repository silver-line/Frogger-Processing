//Most running game logic is contained in the rows.
//We let the row know if the frog is in its row, and if so the row takes care of everything.

class Row{
  float chance;
  float y;
  float w;
  float h;
  float xspeed;
  float cooldown;
  float ccd;
  int r;
  int g;
  int b;
  int rowNum;
  boolean froginrow = false;
  boolean logs;
  ArrayList<Car> cars = new ArrayList();
  
  Row(float chance,float y,float w,float h,float xspeed, boolean logs){
    this.chance = chance;
    this.rowNum = int(y);
    this.y = height - (y * grid);
    this.xspeed = xspeed;
    this.w = w;
    this.h = h;
    
    //Set the minimum cooldown time between when the next log or car can be spawned in the row.
      //Logs have a much higher cooldown to prevent too many logs at a time.
    if(logs){
      this.cooldown = (w*5*map(abs(xspeed),0.75,3,0.75,1));
    }else{
      this.cooldown = 10 + grid*3;
    }
    this.logs = logs;
    ccd = this.cooldown;
    
      //Set Row color
    if(logs){
      //If log row, set random shade of blue
      float rgb = random(1);
      r= int(map(rgb,0,1,28,48));
      g= int(map(rgb,0,1,60,103));
      b= int(map(rgb,0,1,144,255));
    }else{
      //If car row set any random color
      r= int(random(0,255));
      g= int(random(0,255));
      b= int(random(0,255));
    }
  }
  
  void addCar(){
    //Add a car or log, on either the right or left side depending on xspeed direction.
    if(xspeed > 0){
      if(logs){
        cars.add(0,new Log(0-w,y,w,h,xspeed));
      }else{
        cars.add(0,new Car(0-w,y,w,h,xspeed,r,g,b));
      }
    }else{
      if(logs){
        cars.add(0,new Log(width,y,w,h,xspeed));
      }else{
        cars.add(0,new Car(width,y,w,h,xspeed,r,g,b));
      }
    }
  }
  
  void update(){
    testAddNew();
    ArrayList<Car> toRemove = new ArrayList();
    for(Car c: cars){
      c.update();
      if(c.isOffMap()){
        toRemove.add(c);
      }
    }
    cars.removeAll(toRemove);
   hasTouched();
  }
  
  //Helper function to randomly spawn a car or log depending on both the chance variable that is randomly set per row,
    //as well as if the cooldown has been reached.
  void testAddNew(){
    if(ccd > cooldown){
      if(random(1)<chance){
        addCar();
        ccd = 0;
        
      //if we havent spawned a car or log in over 3 times the cooldown, just spawn one.
      }else if(ccd > cooldown * 3){
        addCar();
        ccd = 0;
      }else{
        ccd++;
      }
    }else{
      ccd++;
    }
  }
  
  void moveDown(){
    for(Car c: cars){
      c.moveDown();
    }
    y = y + 5;
  }
  
  void show(){
    //If the frog is currently in this row, we highlight it slightly to make things easier to see.
    if(froginrow){
      if(logs){
        noStroke();
        fill(r+50,g+50,b+50);
      }else{
        stroke(1);
        fill(200,200,200);
      }
    }else{
      stroke(1);
      if(logs){
        noStroke();
      }
      fill(r,g,b);
    }
    rect(0,y,width,y + h);
    for(Car c: cars){
      c.show();
    }
  }
  
  //Collision Logic
  void hasTouched(){
    if(froginrow){
      if(logs){
        //Logs Logic
        boolean isonlog = false;
        for(Car c: cars){
          if((frog.left < c.right && frog.left > c.left)
          || (frog.right > c.left && frog.right < c.right)){
             isonlog = true;
          }
        }
        if(isonlog){
         frog.logMove(xspeed); 
        }else{
           ResetFrog(true); 
        }
       }else{
        //Car Logic
        for(Car c: cars){
          if((frog.left < c.right && frog.left > c.left)
          || (frog.right > c.left && frog.right < c.right)){
             ResetFrog(true);
          }
        }
      }
    }
  }
  
}