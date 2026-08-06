

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;





public class Robot extends TimedRobot {

  private final VictorSPX dmotor1 = new VictorSPX(1);
  private final VictorSPX dmotor2 = new VictorSPX(2);
  private final VictorSPX emotor1 = new VictorSPX(3);
  private final VictorSPX emotor2 = new VictorSPX(4); 

  double vel, ve, vd;
  int angulo;
  double tigreD, tigreE, anlE1, anlE2, anlD1, anlD2,velD, velE;
  double dz = 0.04;

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


    anlD1 = sim.getRawAxis(0); 
    anlD2 = sim.getRawAxis(1);
    anlE1 = sim.getRawAxis(4); 
    anlE2 = sim.getRawAxis(5);

    Triggers();
    pov();
    if(sim.getPOV() == -1){
     Triggers();
    }
    else if(tigreD <= 0 && tigreE <= 0){
      pov();
    }
    drive(vd,ve);
    execute();

  }

  public void Triggers(){
    if (sim.getRawAxis(3) > dz){
      vd = tigreD;
      ve = tigreD;
    }
    else if (sim.getRawAxis(2) > -dz){
      ve = tigreE;
      vd = tigreE;
    }
    else{
      vd = 0;
      ve = 0;
    }
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

    public void anlesq() {
      if(anlE1 > dz && anlE2 > dz){

      }

    }
    public void anldir() {
      if(anlD1 > dz && anlD2 > dz){

      }
    }
  }