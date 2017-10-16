
import {environment} from "../environments/environment";

let host = 'localhost:3000';
if(environment.production) {
  host = 'itworkswell.pl:3000'
}

export const SERVER_URL = `http://${host}`;
