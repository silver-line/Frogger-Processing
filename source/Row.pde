class Row{
  float chance;
  float cChance;
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
  this.cChance = chance;
  this.rowNum = int(y);
  this.y =height - (y * grid);
  this.xspeed = xspeed;
  this.w = w;
  this.h = h;
  if(logs){
    this.cooldown = (w*5*map(abs(xspeed),0.75,3,0.75,1));
  }else{
    this.cooldown = 10 + grid*3;
  }
  this.logs = logs;
  ccd = this.cooldown;
  if(logs){
    float rgb = random(1);
    r= int(map(rgb,0,1,28,48));
    g= int(map(rgb,0,1,60,103));
    b= int(map(rgb,0,1,144,255));
  }else{
    r= int(random(0,255));
    g= int(random(0,255));
    b= int(random(0,255));
  }
  }
  void addCar(){
    if(xspeed > 0){
      if(logs){
        cars.add(0,new Log(0-w,y,w,h,xspeed,random(1)));
      }else{
        cars.add(0,new Car(0-w,y,w,h,xspeed,r,g,b));
      }
    }else{
      if(logs){
        cars.add(0,new Log(width,y,w,h,xspeed,random(1)));
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
  
  void testAddNew(){
    if(ccd > cooldown){
      if(random(1)<chance){
        addCar();
        ccd = 0;
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