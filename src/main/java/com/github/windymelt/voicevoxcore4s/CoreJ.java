package com.github.windymelt.voicevoxcore4s;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
import com.sun.jna.Library;

public interface CoreJ extends Library {
	// TODO: Implement constructor
	public static class VoicevoxInitializeOptions extends Structure {
		public static class ByValue extends VoicevoxInitializeOptions implements Structure.ByValue { }
		public static class ByReference extends VoicevoxInitializeOptions implements Structure.ByReference { }

		public short acceleration_mode; // enum
		public short cpu_num_threads; // uint16
		public boolean load_all_models;
		public String open_jtalk_dict_dir;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("acceleration_mode", "cpu_num_threads", "load_all_models", "open_jtalk_dict_dir");
		}
	}
	
	public static class VoicevoxAudioQueryOptions extends Structure {
		public static class ByValue extends VoicevoxAudioQueryOptions implements Structure.ByValue { }
		public static class ByReference extends VoicevoxAudioQueryOptions implements Structure.ByReference { }
		
		public boolean kana;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("kana");
		}
	}

	public static class VoicevoxSynthesisOptions extends Structure {
		public static class ByValue extends VoicevoxSynthesisOptions implements Structure.ByValue { }
		public static class ByReference extends VoicevoxSynthesisOptions implements Structure.ByReference { }

		public boolean enable_interrogative_upspeak;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("enable_interrogative_upspeak");
		}
	}

	public static class VoicevoxTtsOptions extends Structure {
		public static class ByValue extends VoicevoxTtsOptions implements Structure.ByValue { }
		public static class ByReference extends VoicevoxTtsOptions implements Structure.ByReference { }

		public boolean kana;
		public boolean enable_interrogative_upspeak;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("kana", "enable_interrogative_upspeak");
		}
	}
}