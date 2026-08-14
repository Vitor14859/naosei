

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;





public class Robot extends TimedRobot {

  private final VictorSPX dmotor1 = new VictorSPX(1);
  private final VictorSPX dmotor2 = new VictorSPX(2);
  private final VictorSPX emotor1 = new VictorSPX(3);
  private final VictorSPX emotor2 = new VictorSPX(4); 

  double vel, ve, vd;
  int angulo;
  double tigreD, tigreE, velD, velE, cal1,cal4;
  double eixoEx, eixoEy, eixoDx, eixoDy;
  double dz = 0.1;
  double sen, sen1;
  Timer tmoto = new Timer();

  Joystick sim = new Joystick(0);
  boolean A, B, C, D;

  public Robot() {
    dmotor1.setInverted(true);
    dmotor2.setInverted(true);

    dmotor2.follow(dmotor1);
    emotor2.follow(emotor1);

    dmotor1.setNeutralMode(NeutralMode.Brake);
    dmotor2.setNeutralMode(NeutralMode.Brake);
    emotor1.setNeutralMode(NeutralMode.Brake);
    emotor2.setNeutralMode(NeutralMode.Brake);
    
    emotor1.configNeutralDeadband(dz);
    emotor2.configNeutralDeadband(dz);
    dmotor1.configNeutralDeadband(dz);
    dmotor2.configNeutralDeadband(dz);
  }

  @Override
  public void teleopPeriodic() {


    angulo = sim.getPOV();
    
    A = sim.getRawButton(1);
    B = sim.getRawButton(2);
    C = sim.getRawButton(3);
    D = sim.getRawButton(4);

    if (A) {
      vel = 0.25;
    } else if (B) { 
      vel = 0.5;
    } else if (C) { 
      vel = 0.75;
    } else if (D) { 
      vel = 1.0;
    }

    tigreD = sim.getRawAxis(3);
    tigreE = sim.getRawAxis(2);
    tigreE *= -1;


    eixoEx = sim.getRawAxis(0); 
    eixoEy = -sim.getRawAxis(1);
    eixoDx = sim.getRawAxis(4); 
    eixoDy = -sim.getRawAxis(5);

    caulculodir();
    caulculoesq();
  
    if(cal1 > dz){
     anlesq();
    }
    else if (cal4 > dz){
      anldir();
    }
    else if(tigreD > dz || tigreE < -dz){
      Triggers();
    }
    else if (sim.getPOV() != -1) {
        pov();
    }
    else{
      vd = 0; ve = 0;
    }
    drive(vd,ve);
    execute();

  }

  public void Triggers(){
    if (tigreD > dz){
      vd = tigreD;
      ve = tigreD;
    }
    else if (tigreE < -dz){
      ve = tigreE;
      vd = tigreE;
    }
    else{
      vd = 0;
      ve = 0;
    }
    vd *= vel;
    ve *= vel;
  }


  public void drive(double rightVel, double leftVel){
    velD = rightVel;
    velE = leftVel;

    emotor1.set(ControlMode.PercentOutput, ve);
    dmotor1.set(ControlMode.PercentOutput, vd);
  } 

  public void pov(){
    switch (angulo) {
      case 0:  ve = 1; vd = 1;  break;
      case 45: ve = 1; vd = 0.5; break;
      case 90: ve = 1; vd = 0.0; break;
      case 135: ve = -1; vd = 0.5; break;
      case 180: ve = -1; vd = 1; break;
      case 225: ve = -0.5; vd = -1; break;
      case 270: ve = 0; vd = 1; break;
      case 315: ve = 0.5; vd = 1; break;
      default: ve = 0; vd = 0;
    }
    vd *= vel;
    ve *= vel;
  }

    
    public void execute() {
      SmartDashboard.putBoolean("Button A", A);
      SmartDashboard.putBoolean("Button B", B);
      SmartDashboard.putBoolean("Button C", C);
      SmartDashboard.putBoolean("Button D", D);
      SmartDashboard.putNumber("Button Speed", vel);
      SmartDashboard.putNumber("velocidade esquerda", velE);
      SmartDashboard.putNumber("velocidade direita", velD);
      SmartDashboard.putNumber("tigrinho direito", tigreD);
      SmartDashboard.putNumber("tigrinho esquerdo", tigreE);
      
    }
    
    public void caulculoesq() {
      double cal = (eixoEx * eixoEx) + (eixoEy * eixoEy);
      if (cal > 1){
        cal = 1;
      }
      sen = eixoEx / cal1;
      cal1 = Math.sqrt(cal);
            
    }
    public void caulculodir(){ 
    double cal3 = (eixoDx * eixoDx)+ (eixoDy * eixoDy);
    if(cal3 > 1){
      cal3 = 1;
    }
    sen1 = eixoDx / cal4;
    cal4 = Math.sqrt(cal3);
    }

    public void anlesq() {
      if(eixoEx > dz && eixoEy > dz){
        
        vd = cal1 -sen; ve = cal1 ;
      }
      else if(eixoEx < -dz && eixoEy > dz) {
        vd = cal1; ve = cal1 + sen ;
      }
      else if(eixoEx < -dz && eixoEy < -dz){
        vd = -cal1 ; ve = -cal1 - sen ;
      } 
      else if(eixoEx > dz && eixoEy < -dz){
        vd = -cal1 +sen ; ve = -cal1;
      }

      
      else if(eixoEx < dz && eixoEy > dz){
        vd = cal1 ; ve = cal1 ;
      }
      else if(eixoEx > dz && eixoEy < dz){
        vd = 0; ve = cal1 ;
      }
      else if(eixoEx < dz && eixoEy < -dz){
        vd = -cal1; ve = -cal1 ;
      }
      else if(eixoEx < -dz && eixoEy < dz){
        vd = cal1  ; ve = 0;
      }
      
      else{
        vd=0;ve=0;
      }
      vd *= vel;
      ve *= vel;  
      caulculoesq();
    }
    public void anldir() {
     if(eixoDx > dz && eixoDy > dz){
        
        vd = cal4 -sen1; ve = cal4 ;
      }
      else if(eixoDx < -dz && eixoDy > dz) {
        vd = cal4; ve = cal4 + sen1 ;
      }
      else if(eixoDx < -dz && eixoDy < -dz){
        vd = -cal4 ; ve = -cal4 - sen1 ;
      } 
      else if(eixoDx > dz && eixoDy < -dz){
        vd = -cal4 +sen1 ; ve = -cal4;
      }

      
      else if(eixoDx < dz && eixoDy > dz){
        vd = cal4 ; ve = cal4 ;
      }
      else if(eixoDx > dz && eixoDy < dz){
        vd = 0; ve = cal4 ;
      }
      else if(eixoDx < dz && eixoDy < -dz){
        vd = -cal4; ve = -cal4 ;
      }
      else if(eixoDx < -dz && eixoDy < dz){
        vd = cal4  ; ve = 0;
      }
      
      else{
        vd=0;ve=0;
      }
      vd *= vel;
      ve *= vel;  
      caulculodir();
    }

     @Override
  public void autonomousInit(){
    tmoto.reset();
  }

    @Override
    public void autonomousPeriodic(){
      if(tmoto.get() < 2){
        tmoto.start();
        drive(1, 1);
      }
      else{
        drive(0, 0);
    
      }
      execute();
    }
  }