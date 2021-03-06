/*
    Copyright 2007-2014 TSB, http://www.tsbtecnologias.es
    Technologies for Health and Well-being - Valencia, Spain

    See the NOTICE file distributed with this work for additional
    information regarding copyright ownership

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package org.universAAL.lddi.lib.ieeex73std.events;

public interface EventIEEEType {

	/*
	 * Events for managing FSM
	 */

	public final int TRANSPORT_ON = 0;
	public final int TRANSPORT_OFF = 1;
	public final int TIMEOUT_EVENT = 2;

	/*
	 * Events from received APDUs
	 */
	public final int APDU_RECEIVED = 10;
	public final int AARQ_RECEIVED = 11;
	public final int RLRQ_RECEIVED = 12;
	public final int ABRT_RECEIVED = 13;
	public final int DATA_RECEIVED = 14;

	/*
	 * Events from sent APDUs
	 */
	public final int APDU_SENT = 20;
}
