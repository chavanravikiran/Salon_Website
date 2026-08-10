export type HeroMediaType = 'IMAGE' | 'VIDEO';

export interface WebsiteSettings {
  id: number;
  websiteName: string;
  logoImage?: string | null;
  faviconImage?: string | null;
  heroMediaUrl?: string | null;
  heroMediaType?: HeroMediaType | null;
  phone?: string;
  email?: string;
  address?: string;
  description?: string;
  businessHours?: string;
  facebookUrl?: string;
  instagramUrl?: string;
  twitterUrl?: string;
  youtubeUrl?: string;
  googleMapEmbedUrl?: string;
  updatedAt?: string;
}

export interface WebsiteSettingsRequest {
  websiteName: string;
  phone?: string;
  email?: string;
  address?: string;
  description?: string;
  businessHours?: string;
  facebookUrl?: string;
  instagramUrl?: string;
  twitterUrl?: string;
  youtubeUrl?: string;
  googleMapEmbedUrl?: string;
}
