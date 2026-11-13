import { Component } from '@angular/core';
import { MaterialModule } from '../material-module/material-module';

@Component({
  imports: [MaterialModule],
  selector: 'app-footer',
  styleUrl: './footer.scss',
  templateUrl: './footer.html',
})
export class Footer {
  anoAtual = new Date().getFullYear();
}
