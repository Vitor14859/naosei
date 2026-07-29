

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

  Joystick sim = new Joystick(0);

  boolean A;
  boolean B;
  boolean C;
  boolean D;
  double tigreD;
  double tigreE;

  public Robot() {
    dmotor1.setInverted(true);
    dmotor2.setInverted(true);

    dmotor2.follow(dmotor1);
    emotor2.follow(emotor1);

    dmotor1.setNeutralMode(NeutralMode.Brake);
    dmotor2.setNeutralMode(NeutralMode.Brake);
    emotor1.setNeutralMode(NeutralMode.Brake);
    emotor2.setNeutralMode(NeutralMode.Brake);
    
    emotor1.configNeutralDeadband(0.04);
    emotor2.configNeutralDeadband(0.04);
    dmotor1.configNeutralDeadband(0.04);
    dmotor2.configNeutralDeadband(0.04);
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

    tigreD = sim.getRawAxis(2);
    tigreE = sim.getRawAxis(3);
    tigreE *= -1;

    dmotor1.set(ControlMode.PercentOutput, tigreD);
    emotor1.set(ControlMode.PercentOutput, tigreD);

    dmotor1.set(ControlMode.PercentOutput, tigreE);
    emotor1.set(ControlMode.PercentOutput, tigreE);

    pov();
    execute();

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
      SmartDashboard.putNumber("velocidade esquerda", ve);
      SmartDashboard.putNumber("velocidade direita", vd);
      SmartDashboard.putNumber("tigrinho direito", tigreD);
      SmartDashboard.putNumber("tigrinho esquerdo", tigreE);
    }
  }