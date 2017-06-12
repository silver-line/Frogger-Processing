class RowController{
  ArrayList<Row> rows = new ArrayList();
  int hmr;
  int hme;
  boolean moving = false;
  int hasmoved = 0;
  RowController(boolean First){
    hmr = 0;
    hme = 2;
   int start;
    if(First){
      start = 2;
    }else{
      start = 33;
    }
    for(int i=start;i<start + 28;i++){
      if((i > 22 && First) || (i > 22+31 && !First)){
        //Log Row
        addRow(i,true);
      }else{
        //Car Row
        if(i == 22 || i == 22+31){
          //Always one empty row before water section.
        }else if(hmr == 0 && hme == 2){
          addRow(i,false);
          hme = 0;
        }else if(hmr >3){
          hmr = 0;
          hme = 1;
        }else if(hme >1){
          addRow(i,false);
          hme = 0;
        }else if(hme > 0){
          if(random(1)>0.3){
            addRow(i,false);
          }else{
           hme++; 
          }
        }else if(random(1)>0.5){
            addRow(i,false);
        }else{
          hme++;
        }
      }
    } 
  }
  void addRow(int rownum, boolean islog){
    hmr++;
    if(islog){
    //Log Row
    float xspeed = random(0.5,1);
    if(rownum % 2 == 0){
      xspeed = xspeed *-1;
    }
    rows.add(new Row(random(0.001,0.002),rownum,
    random(grid*1.5,grid*3.5),grid,xspeed,true));
    }else{
    //Car Row
    float xspeed;
    if(random(1)>0.8){
      xspeed = random(0.75,1.5);
    }else{
      xspeed = random(0.75,2);
    }
    if(rownum % 2 == 0){
      xspeed = xspeed *-1;
    }
    rows.add(new Row(random(0.01,0.05),rownum,
    random(grid,grid*2),grid,xspeed,false));
    }
  }
  void update(){
    if(!moving){
      for(Row r: rows){
        r.update();
      }
    }else{
      for(Row r: rows){
        r.moveDown();
      }
      hasmoved += 5;
    }
  }
  
  void setFrogRow(){
    for(Row r: rows){
      if(r.rowNum == frogrow){
        r.froginrow = true;
      }else{
        r.froginrow = false;
      }
    }
  }
  
  void show(){
      for(Row r: rows){
        r.show();
      }
  }
  void moveDown(){
    if(!moving){
      moving = true;
    }
  }
  
  void stopMove(){
    if(moving){
      moving = false;
      hasmoved = 0;
      for(Row r: rows){
        r.rowNum = r.rowNum - 31;
      }
    }
  }
}