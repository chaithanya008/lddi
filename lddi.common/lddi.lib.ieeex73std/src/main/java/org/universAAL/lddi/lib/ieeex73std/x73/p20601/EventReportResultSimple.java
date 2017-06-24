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
package org.universAAL.lddi.lib.ieeex73std.x73.p20601;
//

// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net
// Any modifications to this file will be lost upon recompilation of the source ASN.1.
//

import org.universAAL.lddi.lib.ieeex73std.org.bn.*;
import org.universAAL.lddi.lib.ieeex73std.org.bn.annotations.*;
import org.universAAL.lddi.lib.ieeex73std.org.bn.annotations.constraints.*;
import org.universAAL.lddi.lib.ieeex73std.org.bn.coders.*;
import org.universAAL.lddi.lib.ieeex73std.org.bn.types.*;

@ASN1PreparedElement
@ASN1Sequence(name = "EventReportResultSimple", isSet = false)
public class EventReportResultSimple implements IASN1PreparedElement {

	@ASN1Element(name = "obj-handle", isOptional = false, hasTag = false, hasDefaultValue = false)

	private HANDLE obj_handle = null;

	@ASN1Element(name = "currentTime", isOptional = false, hasTag = false, hasDefaultValue = false)

	private RelativeTime currentTime = null;

	@ASN1Element(name = "event-type", isOptional = false, hasTag = false, hasDefaultValue = false)

	private OID_Type event_type = null;

	@ASN1Any(name = "")

	@ASN1Element(name = "event-reply-info", isOptional = false, hasTag = false, hasDefaultValue = false)

	private byte[] event_reply_info = null;

	public HANDLE getObj_handle() {
		return this.obj_handle;
	}

	public void setObj_handle(HANDLE value) {
		this.obj_handle = value;
	}

	public RelativeTime getCurrentTime() {
		return this.currentTime;
	}

	public void setCurrentTime(RelativeTime value) {
		this.currentTime = value;
	}

	public OID_Type getEvent_type() {
		return this.event_type;
	}

	public void setEvent_type(OID_Type value) {
		this.event_type = value;
	}

	public byte[] getEvent_reply_info() {
		return this.event_reply_info;
	}

	public void setEvent_reply_info(byte[] value) {
		this.event_reply_info = value;
	}

	public void initWithDefaults() {

	}

	private static IASN1PreparedElementData preparedData = CoderFactory.getInstance()
			.newPreparedElementData(EventReportResultSimple.class);

	public IASN1PreparedElementData getPreparedData() {
		return preparedData;
	}

}
