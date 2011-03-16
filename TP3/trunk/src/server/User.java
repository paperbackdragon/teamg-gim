package server;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

import util.Command;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	public static enum Status {
		OFFLINE, ONLINE, BUSY, AWAY
	}

	/**
	 * Taken from:
	 * http://stackoverflow.com/questions/153716/verify-email-in-java I hope it
	 * works correctly...
	 */
	private static final Pattern rfc2822 = Pattern
			.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");

	/**
	 * Check if the ID is a valid one
	 * 
	 * @param id
	 * @return True if the user id is valid, false otherwise
	 */
	public static boolean validID(String id) {
		System.out.println(id);
		return rfc2822.matcher(id).matches();
	}

	private String id;
	private String passwordHash;
	private Status status = Status.OFFLINE;
	private String nickname;
	private String personalMessage;

	// It's Horrendous...but it works. I'd like to fix this if I ever get time.
	private String displayPic = "/9j/4AAQSkZJRgABAQEASABIAAD//gATQ3JlYXRlZCB3aXRoIEdJTVD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCABZAHgDASIAAhEBAxEB/8QAHgAAAgMAAwEBAQAAAAAAAAAAAAgGBwkDBAUBCgL/xABLEAABBQABAwIDBAQGDA8AAAAEAQIDBQYHAAgREiETFDEVQWFxFhciUSMyNIGVsRgkM1ZYcpGywdPU8CUnNjhXYnaCkpehtLbS1v/EAB0BAAEEAwEBAAAAAAAAAAAAAAYEBQcIAAIDAQn/xAA4EQACAgIBAwMCBAIHCQAAAAADBAIFAQYHERITABQhFTEIFiJBUWEXIyQylqGxGCU0NlRXZWfw/9oADAMBAAIRAxEAPwD9/HR0dHWes9HS/cmdweU48HtSSDc4JXURjKu71Wx1Vdi8bWXMkEhTKGO5sGEEXujUaGaVaHPAWVg1WLC9jCEWFLN5FvycvhtTfBKiH19OW6uVzUc1tlOz5Wuc9q+zmNOnHc9q/wAZqK37+sheOuOMp3M92/KEHIsLdVxV2pKJx7lsPZOmfS2+3W3tAtPpNEGpDxreW51+Y2FncRTwsbdQMzNTbMJp6V9YVWzmnkLbkdl1njXj9wNXsWwQWaftyYR9xXqWH1yVZ7XNlW3KIVcg1baLG5dJT2jQ16hSmrkwu7EK9oWTadkxqVVTnUqkr7ZNrv563q1VaNuJUsCo18re/u7s1fIdgRClrPBOCSJRMPNthh5oDESBnyxfdrktaIdbVh2I2OeqpYI7u84y2w2tXNtKe+IYnSZ+cGq0lPXkTRvhgtSKxa6Wb0RsnVXoqX7Z8lZevoQL4cia5S5lYLQ1dLEh9tenyx/FiBrRGPakkqx/wks0skQo0PmYmeKNPV1nZyu7twED5A5S7egOMweV+1C1IG5KoOPaqmoZTaKUH5rb8Wb2pqQQxLKt12YHuqgEw8Owho9GIYRUzw32csxh167rL694U4l3FNmbsxh245IM4qx56zqhme4+NpJtbuZ6lyq5BSrysfks+QSxrSYK2+sYRJh5GRTJEuxc2ckcVqWNDd3Ce8M2SJGaPdgpVxlaJheNgZtd0VanSKtzcVpb1DXq9tYpktmq1g39/YobAINGnX3ynV1Hdtp2xCqC5x1WqW76OovP/S9tSt2WamlDWZuTPN09njZVS1F0vNx8AEyL2CshF8ykH5J71cFDolzn6ZcEQ3KFfKfo2VzbRpcISiqjgZrWGtfjR7VHtWBa2XSJK0xWhOf805I1ZDGcp0WuNMpZIDKDT1knwbPOXDGwWQcqe/hWo5WTRPRUdCRA6SGZio9j/ConSP0Hb92bcBcY8ecVco5bjEu85PtKrjiDQ62jCK0+95K0NPO6UCguJ4DbuheVIAY7PjVljXiUcvycQZUVybGQevUd/acQj6Cnkui7C+7XeWM7x0JalyfEs73hLkWnE0fFIlwbG5sZ52ZCZo6CQpRIVUKlqR/iSfAewclot05X0K808m/7cntdVtdtU6/b0+foRbDXbO8IASQxsUms631sVPdYtLA5MEpGaCovco1le6KuPY8dU2+0u7BWh2yg1Kks7pCwbpC6jY3bGK62rKo1+bV78F48/wC8I5RpWbFda1+VOrqExHXmscJsbZ9HUeyl3Fo85T3cKorLEAcnyn75I2qqf5V649Xq6vH1S2dl8eZ8s8INZWAxfM2l1alO+GFVVQjVR5Rxcn7LGIrWRsR8874oIpZWW3t7esoKt+6unlq2qrFiuPvtkwJdVYMcyIUk8/tjGOkYxxKZJ5iMcZTlGOTBZZhxgKqopnYYJEQQjj3TISeekYxx/rnPTGMdcyzjGM59SXo6Ra45R5I5DurKgxFdcacmsJKBtq3EaIXHYPNGjKOk1LpuYZ6m+tdBo4HTvGsAuN8+fUVRoNlVWl0Bbi/LP668UdxL3fPOrOK0crkl+zn7nneU1Gey/AW+TkJkTp/dW/OJRI31ftoEqfsLCUOYdsvJlPpXFdraVAiZHix2Wzf1ojcMSljLCSdXq+25iOOMdpULRiq2BJjBFLOjRaEQUURrfWFizXxZW92cM5jZJqWunvKxYo84gQP1lp2nrHyiJggi4pWLUUCCnDJsyxjGXx6OkBg5B5O44swKnVQ3uIMNIjFqhdnpIN3xdojZ3SrDS1vKjqvP3WSuiVh+DXw7ukDojjDAKoTQG2pMIsjU8dcq1O7QuvkHnpdNUyuFuc/YNWE8AuNzo5YpIno1V9MjHtRzU9Llavj0qitQq0rleq2uzJrdlVWuo7aIBGMUF6McJWK6+BZaZpW4Sx7sa3mHM6L69VsCyhVbF2iUr3kWmVgvYvqGsaWxFaorGGs70Wdr7KqYNiU117mnsl1bGtIyOOSLFKCaTccS9m2xmBMRtXo6OjqVPXH0dJzz73SZ3i6sLKlv4M/UxGWdKNcwVzNJqNXo6of4tjm+N8mpAw9qdWvkHHtdLfl12OpDSRhLSwjeTC5zF8m3pmcwmis631pbOEiq6d0aIsjLm9LHo6iVqKjkX4NjYjSqioqK1i+U8dfnn5usl3fMW1nY8h+e47szuI8SKS0ZzQ6fAWBdJc2Q7xY0Sddfrx9DsZzSnkHzJeQCzkKMEGMNUP8AEtyhe0LCejaw61WtM1alveOIMMIWM1Ldu1Tpk1bFMq9giib8u7ES3cpnK+9XYDQLIPKgsXmVwzkvfJ8Z6cC+QQRs9mvrc1HrK9mDLdWjlBQL11e2CXcOD2K8TlYqiiaclmG7HBWQmAqQcrf1HfhY3ZM0Y3E+t1NY+SORs3I3NFmEwpsT2kDOnxeFy9XRgOgJY2V0LLm0a9WsawiP4bJeul2lc51OW5j5hvNlGFlYOeNKZsTCELlfTU2pK0Wh0HyEphTWOHqiZtddtHNLejA3RBjkOWOaQqJZ/sP8/wDwr/8AXo+xEX6L5/JFXx+fhPZfwX36pZT22x1F8lscDLsWarcWpslq60Tz+cLvKYHZ26yIbm1wNS2sggJa2DxQe+bMOeDHMQlKbflPk/ZLajttm2Zi8zrrbTVOidGsSrq+VgtBJ8aSdUgiNaDaeILl7MZlKIxTnKcxxlhhLXivi/tsr+9bUUnJrNxp+8TS2VlU5gF4cj8mFdm6q3NaSUKYdKZEFb7zSTNtjflUJr6+kroYnWclibPUPOvPeh59qs/UXfH+aySZ/UF6lh9Fb3tjPYmHZ4POFxmD20ssETZAqqnVqh/LxxvAd6YFcXNIkY+xGN+qo3yvt5T0+V/DyieV/Lr79iJ+PhfovpVUX8UXx7p+KdKNhubW9GVIKytZUFFCE6wSwHcd/v7Gzkxhx9Y7qx5uXFhnJEDJ9QHktLEw4zGTFb7PsFirY1aJZU1FbKrKWdMnnyrWAlrU12H3hGBkMTIrVszYsCkCA54FiMf6qOcPhynW8Yd2C9rPMtlypX8fWXbvynQcsXGfPMFmjKIp5qu1tMyeGTY1UgxL7/O08dRpHQksdSvtWiVpD7hiDrVpO5dBd/zjoxuLszyFneUtNk7T5fX2+ip0hr+OKKyzOWewWjIGe2UoKysLAyIqaZrSDmRrGjhGSLUf2Izz4VzfUn3L49Sfd9FTz5+76e/XVPpEQItfHnwPKvuions1fv8ACdLto2y72VYeGU66DHlg7aGOirbBtng05KIZ2q24VdrMRjWMsr5BlWcOp5z7sSgPtdGeQtwg3XXFM5PX7+sY+ofW6uUsPEsY1BqTDY/PEglZZrHG1ZQFD5gwT9WPjprjwpz8bS6HhfNSDuCoeYeL8vyFX0KmEWUGWLuqSoPsaSvPM8mk1YxVg9lepb3kRjsYySR7v2+rN5Js73k7kesxeesLCqfoztNlw7wH1xE5Xj3IoGHyjo6ixgmjnpdhr7+1rcNm7gbwdX58PVWdUVW3AoBbEOyznR8idjKNVfUnbpi/Hj2VVbnMp4VPvT38fl08fb3I4nm8lxckj5ou3bEFwRyK5YozrTmPneTRTw+ry35qawHigMVFST4QgXxGInw3vsXqTTO06/wPoNq02aqttj5dvWM5NLtIHjzkUVTRU5Md2MRra6v2GeaVcHjxT2NNrjlfkE6dXEfojavNn0updIeeLPcKri6jtLAecAYyPZ9KlseztxmvEUhN3YqJytZOHIp9l05OOcSn09OhkslnsPnqrL5eqDpqOmCHAr68EeIYccYaNsUUbIomtYiNaxERETwiIiJ+K6d1u6vczns7RZy2sKU/QnGEF2FUVIEcytqGDesWMsdWlioYWeK90g0sMksQcwz3uHmnika/pUNvhiOXeZ0rzPWzIYWsrB7mRkisQks5JLZ1eLI1PWhR8RAsBbonN+TDDklWWMp4kc9st5XbhquKHXR+0ctDI0daNP8Asw1Q5lgrEY+HEfbrCrVWoklDGIiD3Z6dMdPQjtoWYa/iopYe3ZsCqVKMFv6iABZlghsR8fb4QDRXPico4xgYsSzjHTHT12uD6e65F4bsqfliJ+qprwuyBrP0jRtiZY5yYUeCT5uYpsk5cC2H2kwWY58hMkSORHqEgS9Kns36biS+u55bIk/S8NOoDoLgmVn2jteDrySevp5ruV6RPuLzj2wrZMpY2qsIPOrTsMRdWBdyaYVYaljDDhjwCCQQiiiwxDjDDxMhHHHgY2KGCCGJrY4oYYmNjijja1kcbWsaiNRE6RPuiGrXbDRTENh+K/tk5MYQrvSjvhw8k8NSh+pfKL4Qlz1YvhUR/v6kX6xLzXQY1bjJXb12iM7PxgsLYK22OScWHG6NVpwCxzYzI2EjMYkmyLE8lNr7dxRyN7G4sosPGuVpKW20sUGiMlaeq9LuimlKUrem2NkdWcJ4yzLBJVzzCtzUyJ5CpN1wPESOJGlNx8TqA9ll6fRgva+CyDhnRWqiojnsa530VfvXz/P0dKz2HXZl3wFmpTXufLFDHGiqvq8IjVT0oqqvlqeyJ7r7J9ejqy3p39MBy67xl6xPufu+OWuT96JuaF3j/K1F/m6wQHqHSWmwWRVfK3fbuKaRPU/4s8Wst2TSq937T1kkR0iveiPcrlV3uq9b28v/APJiq/7e8c//ADaj6xgz9ShBWylVE/b5G5DVF9KO+u0vfKe/5J5/r9+qIc+IfUOYJCzHuzDRdZzjr+2PrG39emPn98/w+Ouf456wvz8n7yj45h065ja79Lp+3/C6Nj5x/l1/b9/j1Cs7iBdjusniLC2saGovE0tle21RCNLdQUOPyl1r7camaa14UdtZB0klXXmGQkiVxJrLEgGxiEcATyWgHF+2yWh5G4Mo7PNfquPuwuR+P7C6vtBPa4yqJlQrkOkN0ZljZPPzfokN0IjJR68/IyrdBi1dhnLatNtHLV6B8xYJyePDsrzW36I3z/xL7xfHhPr9PP8AN+HU97MOMZtVx7o7zLmyUO0p9VdFVFyKvokbN84Ux0M7UVGzikMkfETBJ5ZLE97Hezl6jPV+OLbeLPaKulMuK019UdiskwtXdtwIsaWBEvqbSs3qtpODDT9RKvfrELCynBDaJP0pe1Jv4o400nZ+N9xxsdaKFw9txaCs2oYzHtNYwjrdDcVzFeDzjDNWdnYmzdKduCWdfOS+S4kFfEFcHfxlh8BUcpcxZWfZv5Ru6gHiTBqfpaudMRCRMljyLPHl9LQ2RselIbMzPwFOiHFpKCS6jSzj1dNF1yb7GyYrS5+pxcR2szXLhSLwg1CIprc61ONFCJ41uCSJIYYdJlLKwDGksDCUCsc2ZTagg6JCrSKucXIdnO81vNwnIXOd5WWI9LBDX5eiFHHrqivrA2wjwDVVSx3wRomwjDQJGOxIoRhhQh44AxRB4WF5k4S5KqBDJ+CbKrq0sHyEoBZVg9lBUW09RY0E99n3S+JqO3npLayqyLCtlGnJrziwJnvDKJhmPWfw27mHXq60Tms7sZ2yivNcVJQBaqkp5HmvIrcmwuJ2xlCTbOxI2Vq+kga3W/LTlhXaahUbWZWXB/Hz+o0+qJlDSX1RlNgnIw6kpntgYYP3bMKzSwTzlriwKaOphnLMqeaFeNmPhbsMDzRlF7e83vKrgzQZa13erFoCh+SeashotO0jOay0+blcXlgo9BFj2A5yYiIbMw3WLsBbOtrKux0aTNuzp4PAL4r267sDhR84r7S+CJu67kkcIiTHkcVCI+S15W8LOnywNTXxyR2WdLOhsRdYo+O+YfYH105ek/ab2cAcTV1rc798V/sNLOSVaHWCsmIKILc+QmVzpPLlVz3vX9lEa36NRETx15/OfbDy1a19hluKNpNQZC9cVEWCxvqKrQrKcUi0Cpylejq8awIBALJihaqOKAAKjVhAIskThf8A4YNoSQpJ623T3Vg0uMO0IHKGiRSsyzjIk615NCNizreFpxrTzspWG1qBQUu1GbO2sr4Fiu2ThvivZgUyaNdPRhUTCa83KRKB3to1oXhE+nfS84fNtDQgSYS2WUp5E400NqBFMgwFNuKLyn5D7ja+zxsBg/DHbniBcNnrM6VTCJ6zI1oddOSSciKhpUo9KGwiWPzDPaobIGqiTjuXRfgsQuw5NsCHV8QTcHj0qNGZ63JMmq5DuiuR5sb8JY2tQHDU9xToyRZVlbeaDRhyjjKE1ZvD477Ya7g3g0jP0oEVnajz0Vpbi+fhuuw6i5rrSwqZJmoio2wgDkHVHr6H+tGSKjHKvSKG8xY7jy40AUBHdJyHZXml0ml0OVYfR4rPE6bTW891blK7H5DN37RnnlFNEGC5CiiEFkaOxHNHhd0SWdW7xlu3HtOvrO37HQ6fqb7C1rQalsFote7Tu2yu3m4MWhNborhSjxm+odeuFl18ABXAsrFLKn046EhyHfgs7atpVdcrKeKeNgWM+mzsFHSApafUqQNbqNeDFq0F8yv+8jZMzXKPMyFRDBIU5NmhPX7mPUaPLZWGwzLmwkTWY4pVj8tEZ8gK+Kd/rbFOyUZrpyIoRfjERSxxpK6NrUImgkj/AK4a0V3p8pNaX0EKGrblQJYQiRBfbEcA4bfn5o4GRwSTxyLJXSTQsbG5AGR+EkikamffHncAexRyMlo0xT7RkURvEHPcumLpPmVhGHjmyvJA0Oq09ayZRpy7Gq0FTtBZT7F7wNJUACw1L79Z3O8jxMQJnD+SSSNyjsOg554ZZRq1jlYwqH17CK4aC5ESSNslJGckSokgDJvVA03T520P6+Zy/wBnstbcUV9m5plpW2kW0jZlmI3Wa2So7SsGx2FmtK7pah5kccmFGSMxZIJMIbLXXeXbOl23K2QTWXBQ0drt2vvSjmOPfV1xra1klMc8S/utBSsRyzIbIAYx4vTrGmiVwpBppEQoosT55553tjiiijar3vke5URrWtRVVVX7usYO7TmFh+b3u7D9bD+Wwa/hrhQORrkMsMPDbOs9Xug4GKksVdqNQBVuoSC44orCr4+JtApJKy+rZrD2+du6SjjY8PlPb0OxngmX5PgLh19tZVN7YRsnSEfkLYWQtDd3lCsso88uepKbNVB5ASi2+rs6Qs2nNhnb3wNyd3I8oi84c0hLVU1X8JmQyiRMiraGqhZFGGGGNHCONF6BxxoFQcYceGEccQQYUEUUaFBa3JfxAPVuvayqeXGK7sHNq2onZmrv0B4zBigqWB5MpcZu1Ss1DIUDmlTLsWVjbt0d1V0NTsBEghYisU7++RYpR0xCOa7Q2GICvbC8kuQCV5bV2JTnS1NJFidhXK2WBWtjcjRL7FZNIhy6Q9oOIIwnCGTqi2LGS8GCaVjkVHIro2r7oqeU/jdHTLBBwV4g4QsbYhxYmQxRtTw1rI2o1qIifT2To6tF6z1W/LyKuXqvw3vHKr+CfpvRJ/WqJ/P1lfgar5gXUzeP43IfInuqL/fre/u/3/FetU+ZBjZuPLwquZ8Q2klqNMPF49SyvzFzX37oWp7+XTR1z4mp97np9fp1nlgARqvT8k4mWVspI210Gzzr3RzRMtcDyJam7HHWYMpDI22EUNXbNpLI0NZRGaGmu69sqTgEQxVA5OTwXnGMWI5GJ3j/AFsaRidkRtNwut2mZVbMpdSlVAtEzWIxzEMXEsTz3MjxmP8AlZGTVRoJ8wxlcV3utfMnX4G69Xak4kGec46Qm2vV2ZF8df6z2DGMfMOmYSYFW5/kDj/SXlgPSUEDN7nLK+OSRlVTT7bjrVZKmOuSY45Ps+obd3FdDY2pCMAqRZ32dmQJWCGFwXb2o1Ws4Czuvz2ow+xsrMm3szqL7Bz1lZ1ty0smScBIrgUeWtiiISSNry0ImCYiulhIKg9EsntFZkc4eUUsOIgeZislhlax8b2qnhUc13lFRU+5U/D6dVxFwHhBpI3hUs1cyF/xIRq2yPrw4Xer1r8IMQuEaNFd7uRkTUVfK+PPTGDX921q8evdFs0a1y3RKhYTsFhtjiIo64cDgXIseOXUpIDNXGkYag5sOjtK+4EVLFaw6XszmmI2VPLWUtjqrK6DsEYzu2qJxOxxXJ1TI/MGttQuoNpop9Q5Ao0AwpyE52sdoohuOV9BY8hT3cVOLuMNmyD6/li/q2QSaS1uDEijIbwndo+V1VWcMuiZFWIjoReRNAPpwLB0NYRSaNG64v5X1tJWEhXdPpORqOQGC0xG1x1IfdVevqjxmlVM0Jw0Lh4JCo3xRkfMSMUOZZIi2wkQzwsrKux1dU18NVXVw4lePEkEQsLGMhZEien0IxvhEb49vHj6dQkjg7GzkoSlWUM5skkrIQrSwCFjfLM8iV0YopcQ7Flnlkml9MafElkkkd5e9yq10Gl7nozZ7bRLoNfcW65g357IUHxWpGP1e+sfKtI9xbKsS9+lb2JZNxsmbp1z3ktmvBsutVyPuIGrU2zph3JG1cHZLVBbU9Gvq7MMCXwjrBxV1plPXp1wl0S03t4QJNNWwgcT5GzniW+5euX8krYnVrL7J5omypuYttVzWhNzntIbMOMxvFQ1ejnJUcIPD+HdDQrYv31iTraz5L5uvprEtxeLOV9PWBS1m2p9HpgGMgKzm5yGftNRndVRmwRmVV1V2VOKSOWFYgzQFDEwPkhmhmZIjmOc+GKma3EVVRWRU9dVjC1sMPwIxIo42wti9Pp9HoT9nx49vHj/ANffqstJwZjVqbYqKvPEkhri3wNBubUGEdY4HuYg0AhsUQ6NcnljYmMRq+7UTr2g1bduO2nL3R7REVnaLzlskbsM3lr0+MQJh+4mAardxdiJDJRXzDK9gZl+9ZscvSuO1HxLkzcAzuTbOgHb0rBr6nX1mLadBDUpwHgGarXGI1NxAevSQEoD6WROPRpOFjE8WW3Mm0Ij5oxZ3zUAgOzs5RZFGsRA8BryZgJ19SOFsIkp/A0/hrlWGVUf6f2vHpVFWGFanhkMj7UuMboap807Y2m23G+qGjmKk9To4GSupnI+eT0O+FF59b1RUair0jdrlRtniezCuuSrkuF3b2dYGem7tB5LGwbWcYitsLWQcqOSyPbBGkbCzXTzsa6RGvRHu8x4fLRcc6zlt+WstDUuk7Oe4GyjWHRXTlGtatMhPWWwavOd8pa10r3PBsRvhGCvcroJmOXyhGfk3nIMcmzb8dyXx9K/tmNKvfFH6vTV1yDvqPz3gxPbDsxJn7dhB5zCI2LABkgkNUblyjCeM/yBayrOlTkjH57Vi9GVnT11pKMAZ0fK+cAK97aMpFxkkB+XOIyl2Yejbh8Fb+udXXuM008D2eGvj4y2bVan1arV/R9F9lRHIqfgvSmWvaT242E8kkQXJI0MjlX5ePDb5sSNVfKNRjaX0+Pu/i+PH3dQPMcWEHZ2lMI3vLks5NYHNNK/ljkJz5JJIGOfI5XaRFVznKqqqp9/uvnr3f1Qu/v55Z/81+Qf/wBN1ylvvN1mFY7QOMmY5gMwcM6dcHyLyRhPrDybnPEJdM/eOfvjHz61X5eX8UCB49thRNCE+keQ145zicYyxiXZomMZ+JYxn9vnPT4+1sce9v3a3xtO20TL6h0g7kkeZZ8ebGMeFGr6nTTkT0SMijb5V0kj3I1ERznKieV60Dyx2ZOqRZMpPXz1XwmKO6vdGsXw1anpXxH9PKePqnsvt7KiomPOnxNpkDMFd1e95UYRDyrxbBIwjk/dmCEiGb3PiGhGhFaCcUwMwWaUYoUmKSGaGV7Hsci+zE9revsmdwXO+CbK5tDSakhtYEnn4IkUow86xwsT9ljfiSvd4T28uVV/f0ecYcl8gXG+y0vdBawQR9VtL9RihqLGpKqWpsteRwAuXL+6E4Bwd2aXSAUJozrx9CPxfzFAs1rZUdur71kNC9r7dC7QgKJm+XvQvL3y94UZRzFRUhFSqkpJxnCcWYng1GUZByHOC6Q9HR0dWU9O3rinhjIhlglaj45o3xSNciOa5j2q1yKi+yoqKvlF9l+/rNHmvjdMPYDE29PpyczUzET5Hd4IiMXe8eiG2ENlaUYqlA21Vf5KylikQjN3tNaiDyFTm0zqO2VlxHpn11ygxDYnQGDDlwvRUfETDHNG5F+qKyRrmqi/l0A77xzQcgqJitJvV1nVyPOm2CnmqK4qstZBloYMvKWFe0o1NRI56+zr30JPV9XaQWHbVFU8l17lzJt1liglb1FhgOHqqxGQijE155IqxCYCrtpvJlzkiVigyo+pOU/bsjiQsZ48hc0BV0fpl7kOLionvldDDtePNBltCHA53mAeyHptJqQSC4ovDCCIpK9JZGuk+zg/X8tF2/1+1H+EJwB/Q+0/1HWjt72+cNaOZ5Frx3lSZ3qquldSVquVVXyqqqjL9V+vUYXtN4Ad9eNMqv4fYlYqJ+XkVeovhwvvAIxCtygpkA8YgPLur7K23mEenSTDMeTV4FNnGOs5jXAPM/kYRQxEcWLGnaHj+5W7aGHXrEIt5dIIeOuM9o5PVrrWYYz1xHztHJiOekiS+/pCf1+1H+EJwB/Q+0/1HR+v2o/wheAP6H2n+o6fT+xL7f8A/ozyn9B1n+ydfF7S+3/wvjjPKefC+P8AgOr/ANl62/oc37/uhV/4P2b+X/tP+X+n8Pn38n6L/wBFuP8AjaX8v/B/y/zz/H1Q2B0dhcXNPS2VtnNKzR5GbZU13ma61r66auHvj8/LE4e5jjM+IhddM9kvw0ilifG+JXtc1zrP01F4zt4vob7VR6+zPH0GkX9309vfqsuLqkALlrGVULIAw6zifbBBj+Y4ooRg+ZtmNDBG39lrWRRRxxtaxPS1qNaiePHTWamvC/RnQKkontT2K+0sfn2Fl/637+k3Fc39s40qL63IAlg/Q1bjcg4MIMWH9bqbRqIBttONDBBp1jC8GXGziBgYitHnDJJDD+vrpWW5VS0zlSpNivqdCbh8HbykjPEAe5Y7Bec2I5/WXxwxPOfiMcZxGKW4wL5ui7Oo/T6vR222C/cvj+1uNkVfC/X9/wB30+7rxeUa9Q9Vyanp8fE7L+5H38Iir/BY76ePu6sbiOIecHtEZNLCxn9jTZOjWV7WteiRcb+PSrnIip6VRfbz7KiovjrzefoRoNdv2wTQPV3Zd3LOVsUjHqjWx4tFcvpcqoiKqfX290/DyxO1sJaQw/1j1ifTR/fHd/ylqcPt9+vTP/2PUXtJRzqhW/09Yl1iP3x8dNb1yOOmP49M/HT+Xz64wUZn8Rx9/BQOL0heKydV80+SEVbfUmV9LVoTNFFPJEO446BJ5Yx53xRK97YpXNRjowFyNJZWGqzsGH0YOm46p7TRclhW4UoIGLoqkQwxtkXcKySss4dFEAS7GupCLJmsHZLa0ziKIKztgLK3QQkPH/bvNHOKr15f7efDY5Y3Per+QMovhEa9VVfqv0/evt9OkR441vJV/su4et0O4vrfCZ3G8xVGUyhpavqKF8PE+rnmkBh8NRiq6edE9TnoxJH+n0+pVcG7xtFvqV9r1JXrLnSPR0tjYkywIRBKP7bx7pMcKQlWu4K5Cx31GwnIplw5rKyxTjH3jqj9cf0ekHu6zYXRXn0oeq0le97SFWvYYtME1XatgkH3JG1pVpO/V4L4ZwF/GQuGjlTy+FgDVcrhwl4zBXkEathM5N4dMi8oqKkZHIWYkb58qvv6Xefr938/Xh9r3/O87jPw1S/+wD6tTksIOLgbiydswrpF2fBXhGyxK9XP3mTRqIiPVVcqr4anjyq+yefPjqq+1737vO43x7+NU7z4+5fs8JfC/uXwqL+Sp+/qQ9WVytzxQyz25k1w9euT7OnTEj32lSz9uvTHX4x1+2Onom0NfwI7jLPb3MR42Zn29OneVTfpS+3Xpjr16Y/bHrVTo6Ojq0Pop9HR0dHWes9HR0dHWes9HR0dHWes9Zf9xWGucro23I/EcfJoo094ufsRtNu8zd0ddo7WS9tKNJshq86ObWOuJyD4GmRzTRSTuZ8T0Mi9Kuz6W/nilHm7UdDNFMx0UsUnKXNKskjeitcxyfrRVFa5qqioqeFRVRetxb3+Tp+Tv9HUE6gdj8OXGpjnIEF2iE08z9kpcn9mCGYxhFdcbUGSBUCKEAKqRL7dJUYU0xgUXAAe7QaN9g7tnp+k2tg3LBHbGy1SlafeLgcBZO60RTyNMSGOECHNmZS4jjJJzl1l6yHdf8taWrzIFr2oVhFFx7Urm+PAo9lt6qfJZhwdQFJR1peetaW0mCmhoal8zLm1uyJJhGTKV65JXy8VdZcpVZdqTQdpVQMboczb4XQGH7XeXU9niNEo36Q5eSTT3GjQatuFDE+cdWJXWKKNGo1kKvrV2zQX8ld+Sf1r19g/urf9/wB3W3+zjxjmfkkDa5F/TnLEt73HL3dAcAjzGz+tfVBYEEcF1xAdEFRMYq9QYEAhVHrIFJNiLctP0OTcYghBmWj6rIsYKrCTWjjOanMc4AmAKou6OcxCOEOvx19Ytwt5Dpi6Uql7RKkS2zVlW3OXIm3vJliJQXdMREXS2gNbdae3pvj1RcMBIrCaokZr4mteO+Pyxbk4C7buU6rDbLVakAKTWaexvrM2iIimWqsgNIBY1V3nSI0lYW2vPqLMqt9cJEZkMSxyxENmYjk1Cl/u6f4zf87qXC/yZn+Kv9XTjXcB8YVi9osKleNG3RHXMFa2C+KwusJ+utR5qz4sYTqGoWdRUP4sKzKlhlmpqySal9OTwFQideqFNenp9dpVTE8radPrlJWqWEvaHQ7bJZREQbEXsWmlPA7A4MLtMiwPEDlxPDQ+t3GfPCFru0SkmlzdlXWWefJv+Ui6uutaMyE2jsRau01lnUo+sOGGLDiIrShY5II2yDSxeqNzW9kPD/JdDodzyhylAwbSbaylsSh42ObHCr42QQQR+rw57RxoYYfiP8yS/D+LIqyOcqvoX/d/+8v+d1KQv5O381/0dPGmcQaJoVoa712tbFalrzVWHHbazsZArmTpNMqKhcaKstBk9dXkZIEMTn9krApZjAKEeAYpJqlSq6ago1WDLsNAoaKppYNnUEcKhG81qi0mcqiaZgtg0pwBhk/ijHJSZl2+jo6OpN9a+v/Z";
	private ConcurrentHashMap<String, User> friendList = new ConcurrentHashMap<String, User>();
	private ConcurrentHashMap<String, User> inFreindList = new ConcurrentHashMap<String, User>();
	private ConcurrentHashMap<String, User> friendRequests = new ConcurrentHashMap<String, User>();
	private ConcurrentHashMap<String, User> blockedUsers = new ConcurrentHashMap<String, User>();
	private ConcurrentHashMap<Integer, Room> rooms = new ConcurrentHashMap<Integer, Room>();

	private ConcurrentLinkedQueue<Command> queue = new ConcurrentLinkedQueue<Command>();
	private volatile boolean online = false;

	private transient Worker worker = null;

	/**
	 * Constructor with the minimum possible details
	 * 
	 * @param id
	 *            The user ID of the user
	 * @param passwordHash
	 *            The password hash of the user
	 */
	public User(String id, String passwordHash) {
		this.id = id.toLowerCase();
		this.passwordHash = passwordHash;
		this.status = Status.OFFLINE;
		this.nickname = this.id;
		this.personalMessage = "";
	}

	/**
	 * Constructor with all of the details
	 * 
	 * @param id
	 *            The user ID of the user
	 * @param passwordHash
	 *            The password hash of the user
	 * @param status
	 *            The Status of the user as an enum from User.status
	 * @param nickname
	 *            The nickname of the user
	 * @param personalMessage
	 *            The personal message of the user
	 * @param friendList
	 *            The friend list of the user
	 * @param inFreindList
	 *            A list of users who have this user in their friend list
	 * @param blockedUsers
	 *            A list of users this user has blocked
	 */
	public User(String id, String passwordHash, Status status, String nickname, String personalMessage,
			ConcurrentHashMap<String, User> friendList, ConcurrentHashMap<String, User> inFreindList,
			ConcurrentHashMap<String, User> blockedUsers) {

		this.id = id.toLowerCase();
		this.passwordHash = passwordHash;
		this.status = status;
		this.nickname = nickname;
		this.personalMessage = personalMessage;
		this.friendList = friendList;
		this.inFreindList = inFreindList;
		this.blockedUsers = blockedUsers;

	}

	/**
	 * Put the the user into the list of people this user has sent friend
	 * request to
	 * 
	 * @param user
	 *            The user to add
	 */
	public void addFreindRequest(User user) {
		this.friendRequests.put(user.getId(), user);
	}

	/**
	 * Add a user to this user's friend list and place this user into the list
	 * of people who have that user in their friend list.
	 * 
	 * @param user
	 *            The user to add as a friend.
	 */
	public void addFriend(User user) {
		this.friendList.put(user.getId(), user);
		this.friendRequests.remove(user.getId());

		Worker w = this.getWorker();
		if (w != null)
			w.putResponse(new Command("UPDATE", "FRIENDLIST", Command.encode(this.getId())));

		user.addToInFriendList(this);
	}

	/**
	 * Add a room to the list of rooms that this user is currently in
	 * 
	 * @param room
	 *            the room to add
	 */
	public void addRoom(Room room) {
		this.rooms.put(room.getId(), room);
	}

	/**
	 * Add the user to this users list of people who have them as a friend
	 * 
	 * @param user
	 *            The user to add to the list
	 */
	public void addToInFriendList(User user) {
		this.inFreindList.put(user.getId(), user);
	}

	/**
	 * Add a user to this users list of blocked users
	 * 
	 * @param user
	 *            the user to block
	 */
	public void block(User user) {
		this.blockedUsers.put(user.getId(), user);
		Worker w = this.getWorker();
		if (w != null)
			w.putResponse(new Command("UPDATE", "FRIENDLIST", Command.encode(this.getId())));
	}

	/**
	 * Get a list of all the blocked users
	 * 
	 * @return A Collection of the blocked users
	 */
	public Collection<User> getBlockedUsers() {
		return this.blockedUsers.values();
	}

	/**
	 * Get the display picture as a BASE64 encoded string
	 * 
	 * @return The display picture as a BASE64 encoded string
	 */
	public String getDisplayPic() {
		return this.displayPic;
	}

	/**
	 * Get a list of all the users this user is friends with
	 * 
	 * @return A Collection of the users this user is friends with
	 */
	public Collection<User> getFriendList() {
		return this.friendList.values();
	}

	/**
	 * Get it ID of the user
	 * 
	 * @return The ID of the user
	 */
	public String getId() {
		return this.id;
	}

	public Collection<User> getInFreindList() {
		return this.inFreindList.values();
	}

	/**
	 * Get the users nickname
	 * 
	 * @return Their nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Get the users password hash
	 * 
	 * @return The password hash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Get the users personal message
	 * 
	 * @return The personal message
	 */
	public String getPersonalMessage() {
		return personalMessage;
	}

	/**
	 * YOU MUST FOR ALL THINGS IN THIS WORLD SYNCHRONIZE ANY ADDITIONS TO THIS
	 * 
	 * @return The list of queued commands to the user
	 */
	public ConcurrentLinkedQueue<Command> getQueue() {
		return this.queue;
	}

	/**
	 * Get a list of the rooms this user is in
	 * 
	 * @return A collection of the rooms
	 */
	public Collection<Room> getRooms() {
		return this.rooms.values();
	}

	/**
	 * Get the users status
	 * 
	 * @return The status of the user
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Get a list of the users who have this user in their friend list and who
	 * are currently online
	 * 
	 * @return A hashmap of the users who are online or who this user is
	 *         currently interacting with
	 * 
	 */
	private Collection<User> getUsersToNotify() {
		Collection<User> users = new LinkedList<User>();

		for (User user : this.getInFreindList()) {
			if (user.isOnline() && !blockedUsers.contains(user)) {
				users.add(user);
			}
		}

		for (Room room : this.getRooms()) {
			for (User user : room.getUsers()) {
				if (!blockedUsers.contains(user))
					users.add(user);
			}
		}

		return users;
	}

	/**
	 * Get the current worker for this user
	 * 
	 * @return A worker if the user is logged in, null otherwise.
	 */
	public Worker getWorker() {
		return this.worker;
	}

	/**
	 * Check if a user is in a room
	 * 
	 * @param roomID
	 *            The room ID to check against
	 * @return True if the user is in the room, false otherwise
	 */
	public boolean inRoom(int roomID) {
		return this.rooms.containsKey(new Integer(roomID));
	}

	/**
	 * Check if the user is in a room with another user
	 * 
	 * @param user
	 *            The user to look for
	 * @return True if they are in the same room as the user, false otherwise
	 */
	public boolean inRoomWith(User user) {
		for (Room room : this.rooms.values()) {
			if (room.inRoom(user))
				return true;
		}
		return false;
	}

	/**
	 * Check if the current user is friends with another user
	 * 
	 * @param user
	 *            The user to check against
	 * @return True if the user has the given user in their friend list, false
	 *         otherwise
	 */
	public boolean isFriendsWith(User user) {
		return this.friendList.containsKey(user.getId());
	}

	/**
	 * Check if the user is online (This is their action online state, not just
	 * their status. Appear offline counts as online)
	 * 
	 * @return True if they are online, false otherwise
	 */
	public boolean isOnline() {
		return this.online;
	}

	public void login(Worker w) {
		this.setOnline(true);
		this.setWorker(w);
	}

	/**
	 * Leave any rooms that the user is currently in and logout.
	 */
	public void logout() {
		for (Room room : this.getRooms()) {
			room.leave(this);
		}

		setOnline(false);
	}

	/**
	 * Set the user to not be in the other users friend list
	 * 
	 * @param user
	 *            The user to remove
	 */
	public void notInFriendList(User user) {
		this.inFreindList.remove(user.getId());
	}

	/**
	 * From a user from this users friend list
	 * 
	 * @param user
	 *            The user to remove
	 */
	public void removeFriend(User user) {
		this.friendList.remove(user.getId());
		this.getWorker().putResponse(new Command("UPDATE", "FRIENDLIST", Command.encode(this.getId())));
	}

	/**
	 * Remove a user from the list of pending requests
	 * 
	 * @param user
	 *            The user to remove
	 */
	public void removeFriendRequest(User user) {
		this.friendRequests.remove(user.getId());
	}

	/**
	 * Remove a room from the list of rooms this user is in
	 * 
	 * @param room
	 *            The room to remove
	 */
	public void removeRoom(Room room) {
		this.rooms.remove(room.getId());
	}

	/**
	 * Send a friend request to this user
	 * 
	 * @param from
	 *            The person the request was from
	 */
	public void sendFriendRequest(User from) {
		Command cmd = new Command("FRIENDREQUEST", null, Command.encode(from.getId()) + " "
				+ Command.encode(from.getNickname()));

		from.addFreindRequest(this);

		// Queue the message if the user is offline
		if (this.worker == null) {
			this.queue.add(cmd);
		} else {
			worker.putResponse(cmd);
		}
	}

	/**
	 * Send a message to this user
	 * 
	 * @param from
	 *            Who the message is from
	 * @param roomID
	 *            The room Id this message belongs to
	 * @param message
	 *            The message itself
	 */
	public void sendMessage(User from, int roomID, String message) {
		if (!this.blockedUsers.containsKey(from.getId()) && this.worker != null) {
			this.worker.putResponse(new Command("MESSAGE", null, roomID + " " + Command.encode(from.getId()) + " "
					+ Command.encode(message)));
		}
	}

	/**
	 * Send a command to all users who have this user in their friend list and
	 * who are online
	 * 
	 * @param cmd
	 *            The command to send
	 */
	public void sendToAll(Command cmd) {
		for (User user : getUsersToNotify()) {
			Worker w = user.getWorker();
			if (w != null)
				w.putResponse(cmd);
			else
				user.logout();
		}
	}

	/**
	 * Check if this user sent a friend request to another
	 * 
	 * @param user
	 *            The user to check for
	 * @return True if they did, false otherwise
	 */
	public boolean sentFriendRequestTo(User user) {
		return this.friendRequests.containsKey(user.getId());
	}

	/**
	 * Set the display picture for this user
	 * 
	 * @param displayPic
	 *            The new display picture BASE64 encoded
	 */
	public void setDisplayPic(String displayPic) {
		this.displayPic = displayPic;
		this.sendToAll(new Command("UPDATE", "DISPLAY_PIC", Command.encode(this.getId())));
	}

	/**
	 * Set the Id of the user
	 * 
	 * @param id
	 *            The new ID of the user
	 */
	public void setId(String id) {
		this.id = id.toLowerCase();
	}

	/**
	 * Set the users nickname
	 * 
	 * @param nickname
	 *            the new nickname
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;

		this.sendToAll(new Command("UPDATE", "NICKNAME", Command.encode(this.getId())));
	}

	/**
	 * Set the users online status
	 * 
	 * @param online
	 *            True if the user is online, false otherwise
	 */
	public void setOnline(boolean online) {
		this.online = online;
		if (!online)
			this.setStatus("OFFLINE");
	}

	/**
	 * Set a new password for the user
	 * 
	 * @param passwordHash
	 *            The hash of the password
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;

	}

	/**
	 * Set the personal message for this user
	 * 
	 * @param personalMessage
	 *            The new personal message
	 */
	public void setPersonalMessage(String personalMessage) {
		this.personalMessage = personalMessage;

		this.sendToAll(new Command("UPDATE", "PERSONAL_MESSAGE", Command.encode(this.getId())));
	}

	/**
	 * Set the status for this user
	 * 
	 * @param status
	 *            The new status
	 */
	public void setStatus(Status status) {
		this.status = status;

		this.sendToAll(new Command("UPDATE", "STATUS", Command.encode(this.getId())));
	}

	/**
	 * Set the status of this user
	 * 
	 * @param status
	 *            The new status
	 * @return True if it was successfully set, false otherwise
	 */
	public boolean setStatus(String status) {

		try {
			this.status = Status.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			return false;
		}

		this.sendToAll(new Command("UPDATE", "STATUS", Command.encode(this.getId())));

		return true;
	}

	/**
	 * Set the Worker for this user
	 * 
	 * @param worker
	 *            The worker currently handling this user
	 */
	public synchronized void setWorker(Worker worker) {
		this.worker = worker;
	}

	/**
	 * Remove a user from this users blocked list
	 * 
	 * @param user
	 *            The user to remove
	 */
	public void unblock(User user) {
		this.blockedUsers.remove(user.getId());
		this.getWorker().putResponse(new Command("UPDATE", "FRIENDLIST", Command.encode(this.getId())));
	}

}
