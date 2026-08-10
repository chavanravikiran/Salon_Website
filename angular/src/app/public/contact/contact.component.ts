import { Component, OnDestroy, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Subscription, interval } from 'rxjs';
import { WebsiteSettingsService } from 'src/app/core/services/website-settings.service';

interface DaySchedule {
  day: number; // JS Date.getDay() index: Sun=0 ... Sat=6
  label: string;
  open: number; // decimal hour, e.g. 9.5 = 9:30 AM
  close: number;
}

interface DisplayDay extends DaySchedule {
  isToday: boolean;
  hoursLabel: string;
}

interface StoreStatus {
  isOpen: boolean;
  closingLabel: string;
  remainingLabel: string;
  nextOpenLabel: string;
}

const WEEK_SCHEDULE: DaySchedule[] = [
  { day: 1, label: 'Mon', open: 9, close: 20 },
  { day: 2, label: 'Tue', open: 9, close: 20 },
  { day: 3, label: 'Wed', open: 9, close: 20 },
  { day: 4, label: 'Thu', open: 9, close: 20 },
  { day: 5, label: 'Fri', open: 9, close: 20 },
  { day: 6, label: 'Sat', open: 9, close: 20 },
  { day: 0, label: 'Sun', open: 10, close: 18 }
];

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html'
})
export class ContactComponent implements OnInit, OnDestroy {
  settings$ = this.settingsService.settings$;

  weekSchedule: DisplayDay[] = [];
  storeStatus!: StoreStatus;

  private clockSub?: Subscription;

  constructor(private settingsService: WebsiteSettingsService, private sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    this.refreshStoreStatus();
    this.clockSub = interval(60000).subscribe(() => this.refreshStoreStatus());
  }

  ngOnDestroy(): void {
    this.clockSub?.unsubscribe();
  }

  safeMapUrl(url: string | undefined): SafeResourceUrl | null {
    return url ? this.sanitizer.bypassSecurityTrustResourceUrl(url) : null;
  }

  private refreshStoreStatus(): void {
    const now = new Date();
    const today = WEEK_SCHEDULE.find(d => d.day === now.getDay())!;
    const nowDecimal = now.getHours() + now.getMinutes() / 60;

    this.weekSchedule = WEEK_SCHEDULE.map(d => ({
      ...d,
      isToday: d.day === now.getDay(),
      hoursLabel: `${this.formatHour(d.open)} - ${this.formatHour(d.close)}`
    }));

    const isOpen = nowDecimal >= today.open && nowDecimal < today.close;

    if (isOpen) {
      this.storeStatus = {
        isOpen: true,
        closingLabel: this.formatHour(today.close),
        remainingLabel: this.formatRemaining(today.close - nowDecimal),
        nextOpenLabel: ''
      };
    } else {
      const opensToday = nowDecimal < today.open;
      const nextDay = opensToday ? today : this.findNextDay(now.getDay());
      this.storeStatus = {
        isOpen: false,
        closingLabel: '',
        remainingLabel: '',
        nextOpenLabel: opensToday
          ? `today at ${this.formatHour(today.open)}`
          : `tomorrow at ${this.formatHour(nextDay.open)}`
      };
    }
  }

  private findNextDay(currentDay: number): DaySchedule {
    const nextIndex = (currentDay + 1) % 7;
    return WEEK_SCHEDULE.find(d => d.day === nextIndex)!;
  }

  private formatHour(decimalHour: number): string {
    const hours24 = Math.floor(decimalHour);
    const minutes = Math.round((decimalHour - hours24) * 60);
    const period = hours24 >= 12 ? 'PM' : 'AM';
    const hours12 = hours24 % 12 === 0 ? 12 : hours24 % 12;
    return minutes === 0 ? `${hours12} ${period}` : `${hours12}:${minutes.toString().padStart(2, '0')} ${period}`;
  }

  private formatRemaining(decimalHours: number): string {
    const totalMinutes = Math.round(decimalHours * 60);
    const h = Math.floor(totalMinutes / 60);
    const m = totalMinutes % 60;
    if (h === 0) {
      return `${m}m`;
    }
    return m === 0 ? `${h}h` : `${h}h ${m}m`;
  }
}
