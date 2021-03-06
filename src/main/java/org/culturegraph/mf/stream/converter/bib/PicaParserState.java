/*
 *  Copyright 2013 Christoph Böhme
 *
 *  Licensed under the Apache License, Version 2.0 the "License";
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.culturegraph.mf.stream.converter.bib;


/**
 * A parser for PICA+ records. Only single records can be parsed as the parser
 * does not recognise end-of-record markers (usually new lines). The initial
 * parser state is FIELD_NAME. All states are valid end states. The parser
 * processes any input, there is no error state.
 * 
 * The parser ignores spaces in field names. They are not included in the
 * field name.
 * 
 * Empty subfields are skipped. For instance, parsing the following input
 * would NOT produce an empty literal: 003@ \u001f\u001e. The parser also
 * skips unnamed fields without any subfields.
 * 
 * @author Christoph Böhme
 * 
 */
enum PicaParserState {
	
	FIELD_NAME {
		@Override
		protected PicaParserState parseChar(final char ch, final PicaParserContext ctx) {
			final PicaParserState next;
			if (ch == PicaConstants.FIELD_DELIMITER) {
				ctx.emitStartEntity();
				ctx.emitEndEntity();
				next = FIELD_NAME;
			} else if (ch == PicaConstants.SUBFIELD_DELIMITER) {
				ctx.emitStartEntity();
				next = SUBFIELD_NAME;
			} else {
				if (ch != ' ') {
					ctx.appendText(ch);
				}
				next = this;
			}
			return next;
		}
		
		@Override
		protected void endOfInput(final PicaParserContext ctx) {
			ctx.emitStartEntity();
			ctx.emitEndEntity();
		}
	},
	SUBFIELD_NAME {
		@Override
		protected PicaParserState parseChar(final char ch, final PicaParserContext ctx) {
			final PicaParserState next;
			if (ch == PicaConstants.FIELD_DELIMITER) {
				ctx.emitEndEntity();
				next = FIELD_NAME;
			} else if (ch == PicaConstants.SUBFIELD_DELIMITER) {
				next = this;
			} else {
				ctx.setSubfieldName(ch);
				next = SUBFIELD_VALUE;
			}
			return next;
		}
		
		@Override
		protected void endOfInput(final PicaParserContext ctx) {
			ctx.emitEndEntity();
		}
	},
	SUBFIELD_VALUE {
		@Override
		protected PicaParserState parseChar(final char ch, final PicaParserContext ctx) {
			final PicaParserState next;
			if (ch == PicaConstants.FIELD_DELIMITER) {
				ctx.emitLiteral();
				ctx.emitEndEntity();
				next = FIELD_NAME;
			} else if (ch == PicaConstants.SUBFIELD_DELIMITER) {
				ctx.emitLiteral();
				next = SUBFIELD_NAME;
			} else {
				ctx.appendText(ch);
				next = this;
			}
			return next;
		}
		
		@Override
		protected void endOfInput(final PicaParserContext ctx) {
			ctx.emitLiteral();
			ctx.emitEndEntity();
		}
	};

	protected abstract PicaParserState parseChar(final char ch, final PicaParserContext ctx);
	
	protected abstract void endOfInput(final PicaParserContext ctx);
	
}