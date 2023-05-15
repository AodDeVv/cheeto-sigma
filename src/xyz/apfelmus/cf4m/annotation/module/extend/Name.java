package xyz.apfelmus.cf4m.annotation.module.extend;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
  String name();
}


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cf4m\annotation\module\extend\Name.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */